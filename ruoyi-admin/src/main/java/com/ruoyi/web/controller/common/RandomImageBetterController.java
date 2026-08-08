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
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
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
     * 图片时间每经过30天，随机权重衰减为原来的一半。
     */
    private static final long WEIGHT_HALF_LIFE_MILLIS = TimeUnit.DAYS.toMillis(30);

    /**
     * 为时间未知或时间过早的图片保留一个最小权重，确保它们仍有机会被选中。
     */
    private static final double MIN_IMAGE_WEIGHT = 0.000001D;

    /**
     * 按目录后缀缓存图片列表，避免每次请求都扫描目录。
     */
    private static final ConcurrentHashMap<String, File[]> imageCache = new ConcurrentHashMap<>();

    /**
     * 获取一张随机图片。
     *
     * @param request HTTP请求；支持 {@code imgUrlSuffix} 和 {@code useTimeWeight} 参数
     * @return 图片资源；目录无图片或资源不可用时返回404，读取异常时返回500
     */
    @RateLimiter(time = 60, count = 20, limitType = LimitType.IP)
    @GetMapping(value = "/getRandomImageBetter", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getRandomImageBetter(HttpServletRequest request) {
        String imgUrlSuffix = request.getParameter("imgUrlSuffix");
        if (imgUrlSuffix == null || imgUrlSuffix.isEmpty()) {
            imgUrlSuffix = DEFAULT_IMAGE_URL_SUFFIX;
        }

        boolean useTimeWeight = isTimeWeightEnabled(request.getParameter(USE_TIME_WEIGHT_PARAMETER));
        File imageDirectory = new File(IMAGE_DIRECTORY_PATH + imgUrlSuffix);
        File[] images = imageCache.computeIfAbsent(imgUrlSuffix, key -> imageDirectory.listFiles());

        if (images == null || images.length == 0) {
            log.warn("随机图片目录不存在、不可读或不包含图片：{}", imageDirectory.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        File randomImage = selectRandomImage(images, useTimeWeight);
        try {
            Resource imageResource = new UrlResource(randomImage.toURI());
            if (imageResource.exists() || imageResource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
            }

            log.warn("随机选中的图片不存在或不可读：{}", randomImage.getAbsolutePath());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("加载随机图片资源失败：{}", randomImage.getAbsolutePath(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
     * 按请求模式选取图片。关闭时间权重时每张图片概率相同；开启时使用30天半衰期计算权重。
     *
     * @param images        候选图片
     * @param useTimeWeight 是否启用时间权重
     * @return 被选中的图片
     */
    private File selectRandomImage(File[] images, boolean useTimeWeight) {
        Random random = new Random();
        if (!useTimeWeight) {
            return images[random.nextInt(images.length)];
        }

        long currentTimeMillis = System.currentTimeMillis();
        double[] weights = new double[images.length];
        double totalWeight = 0D;
        for (int i = 0; i < images.length; i++) {
            long imageTimeMillis = readImageTimeMillis(images[i]);
            double weight = calculateTimeWeight(currentTimeMillis, imageTimeMillis);
            weights[i] = weight;
            totalWeight += weight;
        }

        double randomWeight = random.nextDouble() * totalWeight;
        for (int i = 0; i < images.length; i++) {
            randomWeight -= weights[i];
            if (randomWeight < 0D) {
                return images[i];
            }
        }

        // 浮点数舍入可能使随机值未在循环中归零，等概率随机一张作为兜底，避免固定图片偏置。
        return images[random.nextInt(images.length)];
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
            log.warn("读取图片时间属性失败，将使用最低随机权重：{}", image.getAbsolutePath(), e);
        }
        return 0L;
    }

    /**
     * 根据图片距当前时间的时长计算指数衰减权重。未来时间按当前时间处理。
     *
     * @param currentTimeMillis 当前时间戳
     * @param imageTimeMillis   图片时间戳
     * @return 大于0的随机权重
     */
    private double calculateTimeWeight(long currentTimeMillis, long imageTimeMillis) {
        if (imageTimeMillis <= 0L) {
            return MIN_IMAGE_WEIGHT;
        }

        long ageMillis = Math.max(0L, currentTimeMillis - imageTimeMillis);
        double weight = Math.pow(0.5D, (double) ageMillis / WEIGHT_HALF_LIFE_MILLIS);
        return Math.max(weight, MIN_IMAGE_WEIGHT);
    }
}
