package com.ruoyi.web.controller.common;

import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.enums.LimitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 随机图片接口。
 *
 * <p>默认根据图片的最后修改时间进行加权随机，图片越新，被选中的概率越高；调用方也可以
 * 通过 {@code useTimeWeight=N} 切换为所有图片等概率随机。</p>
 */
@RestController
public class RandomImageBetterController {

    private static final Logger log = LoggerFactory.getLogger(RandomImageBetterController.class);

    private static final String IMAGE_DIRECTORY_PATH = "/home/ruoyi/randomImages";
    private static final String DEFAULT_IMAGE_URL_SUFFIX = "/forBlog";
    private static final String USE_TIME_WEIGHT_PARAMETER = "useTimeWeight";
    private static final String ENABLE_TIME_WEIGHT = "Y";
    private static final String DISABLE_TIME_WEIGHT = "N";

    /**
     * 单个IP每分钟允许访问300次，满足正常连续刷新，同时拦截明显的高频滥用。
     */
    private static final int RANDOM_IMAGE_RATE_LIMIT_COUNT = 300;

    /**
     * 图片目录快照每30秒刷新一次，兼顾文件变化感知和高频请求性能。
     */
    private static final long IMAGE_CACHE_REFRESH_MILLIS = TimeUnit.SECONDS.toMillis(30);

    /**
     * 图片时间每经过30天，新鲜度加成衰减为原来的一半。
     */
    private static final long WEIGHT_HALF_LIFE_MILLIS = TimeUnit.DAYS.toMillis(30);

    /**
     * 每张图片固定保留20%的基础权重，确保旧图片仍有稳定的展示机会。
     */
    private static final double BASE_IMAGE_WEIGHT = 0.2D;

    /**
     * 最新图片最多获得额外80%的新鲜度权重，因此单张新图片的权重最多是旧图片的5倍。
     */
    private static final double FRESHNESS_WEIGHT = 0.8D;

    private static final ImageEntry[] EMPTY_IMAGE_ENTRIES = new ImageEntry[0];

    /**
     * 按目录后缀缓存图片及其时间权重，避免高频请求重复扫描目录和读取文件属性。
     */
    private static final ConcurrentHashMap<String, ImageSnapshot> imageCache = new ConcurrentHashMap<>();

    /**
     * 获取一张随机图片。
     *
     * @param request HTTP请求；支持 {@code imgUrlSuffix} 和 {@code useTimeWeight} 参数
     * @return 图片资源；目录无图片或资源不可用时返回404，读取异常时返回500
     */
    @RateLimiter(time = 60, count = RANDOM_IMAGE_RATE_LIMIT_COUNT, limitType = LimitType.IP)
    @GetMapping(value = "/getRandomImageBetter", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getRandomImageBetter(HttpServletRequest request) {
        String imgUrlSuffix = request.getParameter("imgUrlSuffix");
        if (imgUrlSuffix == null || imgUrlSuffix.isEmpty()) {
            imgUrlSuffix = DEFAULT_IMAGE_URL_SUFFIX;
        }

        boolean useTimeWeight = isTimeWeightEnabled(request.getParameter(USE_TIME_WEIGHT_PARAMETER));
        File imageDirectory = new File(IMAGE_DIRECTORY_PATH + imgUrlSuffix);
        ImageSnapshot imageSnapshot = getImageSnapshot(imgUrlSuffix, imageDirectory);

        if (imageSnapshot.images.length == 0) {
            return ResponseEntity.notFound().build();
        }

        Resource imageResource = selectReadableImageResource(imgUrlSuffix, imageSnapshot, useTimeWeight);
        if (imageResource == null) {
            log.error("随机图片目录中没有可用的图片资源：{}", imageDirectory.getAbsolutePath());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
    }

    /**
     * 解析是否启用时间权重。未传、空值和Y均启用，N表示等概率随机；其他值按默认Y处理。
     *
     * @param parameterValue useTimeWeight参数值
     * @return 是否启用时间加权
     */
    private boolean isTimeWeightEnabled(String parameterValue) {
        if (parameterValue == null || parameterValue.trim().isEmpty()
                || ENABLE_TIME_WEIGHT.equalsIgnoreCase(parameterValue.trim())) {
            return true;
        }
        if (DISABLE_TIME_WEIGHT.equalsIgnoreCase(parameterValue.trim())) {
            return false;
        }

        log.warn("参数{}不是Y或N，将按默认值Y处理", USE_TIME_WEIGHT_PARAMETER);
        return true;
    }

    /**
     * 获取目录快照。快照未过期时直接复用；过期后仅由当前目录对应的一个线程负责刷新。
     *
     * @param cacheKey       缓存键
     * @param imageDirectory 图片目录
     * @return 可用的图片快照
     */
    private ImageSnapshot getImageSnapshot(String cacheKey, File imageDirectory) {
        long currentTimeMillis = System.currentTimeMillis();
        ImageSnapshot cachedSnapshot = imageCache.get(cacheKey);
        if (cachedSnapshot != null && cachedSnapshot.isFresh(currentTimeMillis)) {
            return cachedSnapshot;
        }

        return imageCache.compute(cacheKey, (key, currentSnapshot) -> {
            long refreshTimeMillis = System.currentTimeMillis();
            if (currentSnapshot != null && currentSnapshot.isFresh(refreshTimeMillis)) {
                return currentSnapshot;
            }

            ImageSnapshot refreshedSnapshot = scanImageDirectory(imageDirectory, refreshTimeMillis);
            if (refreshedSnapshot != null) {
                return refreshedSnapshot;
            }

            // 目录出现短暂访问异常时继续使用旧数据，避免高频请求同时回源扫描。
            return currentSnapshot == null
                    ? new ImageSnapshot(EMPTY_IMAGE_ENTRIES, refreshTimeMillis, 0D)
                    : new ImageSnapshot(currentSnapshot.images, refreshTimeMillis, currentSnapshot.totalTimeWeight);
        });
    }

    /**
     * 扫描目录并一次性读取文件时间，后续请求只使用内存中的文件和权重数据。
     *
     * @param imageDirectory     图片目录
     * @param snapshotTimeMillis 快照生成时间
     * @return 新快照；目录暂时无法访问时返回null
     */
    private ImageSnapshot scanImageDirectory(File imageDirectory, long snapshotTimeMillis) {
        File[] imageFiles;
        try {
            imageFiles = imageDirectory.listFiles(file -> file.isFile() && file.canRead());
        } catch (SecurityException e) {
            log.error("无权限扫描随机图片目录：{}", imageDirectory.getAbsolutePath(), e);
            return null;
        }

        if (imageFiles == null) {
            log.error("随机图片目录不存在或不可读：{}", imageDirectory.getAbsolutePath());
            return null;
        }
        if (imageFiles.length == 0) {
            log.warn("随机图片目录中没有可读文件：{}", imageDirectory.getAbsolutePath());
            return new ImageSnapshot(EMPTY_IMAGE_ENTRIES, snapshotTimeMillis, 0D);
        }

        ImageEntry[] images = new ImageEntry[imageFiles.length];
        double totalTimeWeight = 0D;
        for (int i = 0; i < imageFiles.length; i++) {
            long imageTimeMillis = readImageTimeMillis(imageFiles[i]);
            double timeWeight = calculateTimeWeight(snapshotTimeMillis, imageTimeMillis);
            images[i] = new ImageEntry(imageFiles[i], timeWeight);
            totalTimeWeight += timeWeight;
        }
        return new ImageSnapshot(images, snapshotTimeMillis, totalTimeWeight);
    }

    /**
     * 首次按请求模式选图；若缓存文件已失效，则随机检查其余候选图片，避免直接返回空响应。
     *
     * @param cacheKey      缓存键
     * @param imageSnapshot 图片快照
     * @param useTimeWeight 是否启用时间权重
     * @return 可读图片资源；所有候选图片均失效时返回null
     */
    private Resource selectReadableImageResource(String cacheKey, ImageSnapshot imageSnapshot,
                                                 boolean useTimeWeight) {
        int selectedIndex = selectRandomImageIndex(imageSnapshot, useTimeWeight);
        Resource imageResource = createReadableImageResource(imageSnapshot.images[selectedIndex].file);
        if (imageResource != null) {
            return imageResource;
        }

        // 失效快照不再提供给后续请求，当前请求则继续尝试快照中的其他图片。
        imageCache.remove(cacheKey, imageSnapshot);
        int randomStartIndex = ThreadLocalRandom.current().nextInt(imageSnapshot.images.length);
        for (int offset = 0; offset < imageSnapshot.images.length; offset++) {
            int imageIndex = (randomStartIndex + offset) % imageSnapshot.images.length;
            if (imageIndex == selectedIndex) {
                continue;
            }

            imageResource = createReadableImageResource(imageSnapshot.images[imageIndex].file);
            if (imageResource != null) {
                return imageResource;
            }
        }

        // 所有缓存文件都失效时短暂缓存空结果，防止请求持续扫描磁盘。
        imageCache.putIfAbsent(cacheKey,
                new ImageSnapshot(EMPTY_IMAGE_ENTRIES, System.currentTimeMillis(), 0D));
        return null;
    }

    /**
     * 创建图片资源并检查其是否存在且可读。
     *
     * @param image 图片文件
     * @return 可读资源；文件失效或资源创建失败时返回null
     */
    private Resource createReadableImageResource(File image) {
        try {
            Resource imageResource = new UrlResource(image.toURI());
            if (imageResource.exists() && imageResource.isReadable()) {
                return imageResource;
            }
            log.warn("缓存中的随机图片不存在或不可读：{}", image.getAbsolutePath());
        } catch (Exception e) {
            log.warn("加载随机图片资源失败，将尝试其他图片：{}", image.getAbsolutePath(), e);
        }
        return null;
    }

    /**
     * 按请求模式选取图片索引。关闭时间权重时每张图片概率相同；开启时使用快照中的时间权重。
     *
     * @param imageSnapshot 图片快照
     * @param useTimeWeight 是否启用时间权重
     * @return 被选中的图片索引
     */
    private int selectRandomImageIndex(ImageSnapshot imageSnapshot, boolean useTimeWeight) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        ImageEntry[] images = imageSnapshot.images;
        if (!useTimeWeight) {
            return random.nextInt(images.length);
        }

        double randomWeight = random.nextDouble(imageSnapshot.totalTimeWeight);
        for (int i = 0; i < images.length; i++) {
            randomWeight -= images[i].timeWeight;
            if (randomWeight < 0D) {
                return i;
            }
        }

        // 浮点数舍入可能使随机值未在循环中归零，等概率随机一张作为兜底，避免固定图片偏置。
        return random.nextInt(images.length);
    }

    /**
     * 读取用于加权的图片时间。优先使用最后修改时间；其不可用时回退到创建时间。
     *
     * @param image 图片文件
     * @return 时间戳（毫秒）；无法取得时返回0
     */
    private long readImageTimeMillis(File image) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(image.toPath(), BasicFileAttributes.class);
            FileTime lastModifiedTime = attributes.lastModifiedTime();
            if (lastModifiedTime != null && lastModifiedTime.toMillis() > 0L) {
                return lastModifiedTime.toMillis();
            }

            FileTime creationTime = attributes.creationTime();
            if (creationTime != null && creationTime.toMillis() > 0L) {
                return creationTime.toMillis();
            }
            log.warn("图片缺少有效的最后修改时间和创建时间：{}", image.getAbsolutePath());
        } catch (IOException | SecurityException e) {
            log.warn("读取图片时间属性失败，将仅使用基础随机权重：{}", image.getAbsolutePath(), e);
        }
        return 0L;
    }

    /**
     * 根据图片距当前时间的时长计算权重。所有图片都保留基础权重，新图片额外获得指数衰减加成。
     *
     * @param currentTimeMillis 当前时间戳
     * @param imageTimeMillis   图片时间戳
     * @return 大于0的随机权重
     */
    private double calculateTimeWeight(long currentTimeMillis, long imageTimeMillis) {
        if (imageTimeMillis <= 0L) {
            return BASE_IMAGE_WEIGHT;
        }

        long ageMillis = Math.max(0L, currentTimeMillis - imageTimeMillis);
        double freshness = Math.pow(0.5D, (double) ageMillis / WEIGHT_HALF_LIFE_MILLIS);
        return BASE_IMAGE_WEIGHT + FRESHNESS_WEIGHT * freshness;
    }

    /**
     * 缓存中的图片文件及其时间权重。
     */
    private static class ImageEntry {
        private final File file;
        private final double timeWeight;

        private ImageEntry(File file, double timeWeight) {
            this.file = file;
            this.timeWeight = timeWeight;
        }
    }

    /**
     * 单个图片目录的不可变快照。
     */
    private static class ImageSnapshot {
        private final ImageEntry[] images;
        private final long snapshotTimeMillis;
        private final double totalTimeWeight;

        private ImageSnapshot(ImageEntry[] images, long snapshotTimeMillis, double totalTimeWeight) {
            this.images = images;
            this.snapshotTimeMillis = snapshotTimeMillis;
            this.totalTimeWeight = totalTimeWeight;
        }

        private boolean isFresh(long currentTimeMillis) {
            long cacheAgeMillis = currentTimeMillis - snapshotTimeMillis;
            return cacheAgeMillis >= 0L && cacheAgeMillis < IMAGE_CACHE_REFRESH_MILLIS;
        }
    }
}
