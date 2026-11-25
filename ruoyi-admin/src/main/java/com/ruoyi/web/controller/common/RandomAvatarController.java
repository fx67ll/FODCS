package com.ruoyi.web.controller.common;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.enums.LimitType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成随机马赛克头像
 *
 * @author fx67ll
 */
@RestController
public class RandomAvatarController {

    /**
     * 背景颜色
     */
    private final static Color BACK_GROUND_COLOR = new Color(238, 238, 238);

    /**
     * 图片宽
     * 允许自定义，通过传参体现，这里为默认值
     */
    private final static int IMG_WIDTH = 230;

    /**
     * 图片高
     * 允许自定义，通过传参体现，这里为默认值
     */
    private final static int IMG_HEIGHT = 230;

    /**
     * 图片边缘内边距
     * 允许自定义，通过传参体现，这里为默认值
     */
    private final static int PADDING = 30;

    /**
     * 填充比率，越接近1，有色色块出现几率越高
     */
//    private final static double RADIO = 0.45;

    /**
     * 每边矩形数量（建议>=5）
     * 允许自定义，通过传参体现，这里为默认值
     */
    private final static int BLOCK_NUM = 9;

    /**
     * 颜色差值评价值（越大颜色越鲜艳）
     * 这里我修改为使用随机值而不是静态常量来处理
     */
//    private final static int COLOR_DIFF_EVALUATION = 50;

    /**
     * 基色阶数极限
     */
    private final static int COLOR_LIMIT = 256;

    /**
     * 服务端头像缓存上限。球队列表一页会并发加载多张Logo，按参数缓存可避免翻页时重复绘图。
     */
    private final static int AVATAR_CACHE_LIMIT = 512;

    /**
     * 图片尺寸与马赛克块数量边界。限制异常参数，防止超大画布造成内存或CPU抖动。
     */
    private final static int MIN_IMAGE_SIZE = 64;
    private final static int MAX_IMAGE_SIZE = 1024;
    private final static int MIN_BLOCK_NUM = 4;
    private final static int MAX_BLOCK_NUM = 24;
    private final static int MAX_PADDING = 200;

    /**
     * 按访问顺序淘汰的线程安全头像缓存，仅缓存带 avatarSeed 的稳定图片。
     */
    private final static Map<String, byte[]> AVATAR_CACHE = Collections.synchronizedMap(
            new LinkedHashMap<String, byte[]>(64, 0.75f, true) {
                private static final long serialVersionUID = 1L;

                @Override
                protected boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
                    return size() > AVATAR_CACHE_LIMIT;
                }
            });

    /**
     * 同一种子的渲染锁。冷缓存下若同时到达大量相同Logo请求，只由一个线程生成图片，其余请求等待后复用缓存。
     */
    private final static ConcurrentMap<String, Object> AVATAR_RENDER_LOCKS = new ConcurrentHashMap<>();

    /**
     * 保存路径
     */
//    private final static String DIR = "D://headImg/";

    /**
     * 直接返回随机马赛克图片流。
     * <p>球队Logo通过 avatarSeed 请求稳定图片；带缓存的响应允许浏览器复用，未传种子时仍保持随机语义。
     * Logo属于可缓存的只读资源，接口不在应用层按IP计数拒绝请求，避免翻页、刷新或页面切换时出现限流失败。
     * 所有数值参数均做边界收敛，同一种子的冷缓存并发请求只渲染一次，避免大量重复请求形成图像编码风暴。</p>
     */
    @GetMapping("/getRandomAvatar")
    public void getAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String avatarSeed = normalizeSeed(request.getParameter("avatarSeed"));
        int avatarWidth = parseBoundedInt(request.getParameter("avatarWidth"), IMG_WIDTH,
                MIN_IMAGE_SIZE, MAX_IMAGE_SIZE);
        int avatarHeight = parseBoundedInt(request.getParameter("avatarHeight"), IMG_HEIGHT,
                MIN_IMAGE_SIZE, MAX_IMAGE_SIZE);
        int avatarBlockNum = parseBoundedInt(request.getParameter("avatarBlockNum"), BLOCK_NUM,
                MIN_BLOCK_NUM, MAX_BLOCK_NUM);
        int avatarPadding = parseBoundedInt(request.getParameter("avatarPadding"), PADDING,
                0, MAX_PADDING);
        int minSide = Math.min(avatarWidth, avatarHeight);
        if (avatarPadding * 2 >= minSide) {
            avatarPadding = Math.max(0, minSide / 6);
        }
        String mosaicMode = normalizeMosaicMode(request.getParameter("isNeedMoreMosaic"));

        String cacheKey = avatarSeed == null ? null
                : avatarSeed + "|" + avatarWidth + "|" + avatarHeight + "|"
                + avatarPadding + "|" + avatarBlockNum + "|" + mosaicMode;
        byte[] avatar = cacheKey == null ? null : AVATAR_CACHE.get(cacheKey);
        if (avatar == null) {
            avatar = cacheKey == null
                    ? renderAvatar(ThreadLocalRandom.current(), avatarWidth, avatarHeight,
                            avatarPadding, avatarBlockNum, mosaicMode)
                    : renderStableAvatar(cacheKey, avatarSeed, avatarWidth, avatarHeight,
                            avatarPadding, avatarBlockNum, mosaicMode);
        }

        response.setContentType("image/png");
        response.setContentLength(avatar.length);
        response.setHeader("Cache-Control", avatarSeed == null
                ? "no-store" : "public, max-age=86400, immutable");
        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(avatar);
        }
    }

    /**
     * 生成并缓存稳定种子头像。
     * <p>同一缓存键使用独立渲染锁，避免高并发冷启动时重复绘图；缓存命中后直接返回同一组PNG字节。</p>
     */
    private static byte[] renderStableAvatar(String cacheKey, String avatarSeed, int avatarWidth,
                                             int avatarHeight, int avatarPadding, int avatarBlockNum,
                                             String mosaicMode) throws IOException {
        Object renderLock = AVATAR_RENDER_LOCKS.computeIfAbsent(cacheKey, key -> new Object());
        try {
            synchronized (renderLock) {
                byte[] cachedAvatar = AVATAR_CACHE.get(cacheKey);
                if (cachedAvatar != null) {
                    return cachedAvatar;
                }
                long randomSeed = avatarSeed.hashCode() * 31L + avatarWidth * 17L + avatarHeight * 13L
                        + avatarPadding * 7L + avatarBlockNum * 3L + mosaicMode.hashCode();
                byte[] renderedAvatar = renderAvatar(new Random(randomSeed), avatarWidth, avatarHeight,
                        avatarPadding, avatarBlockNum, mosaicMode);
                AVATAR_CACHE.put(cacheKey, renderedAvatar);
                return renderedAvatar;
            }
        } finally {
            AVATAR_RENDER_LOCKS.remove(cacheKey, renderLock);
        }
    }

    /**
     * 返回随机马赛克图片的Base64
     */
    @RateLimiter(time = 60, count = 20, limitType = LimitType.IP)
    @GetMapping("/getRandomAvatarByBase64")
    public AjaxResult getAvatarBase64(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String avatarWidthValue = request.getParameter("avatarWidth");
        String avatarHeightValue = request.getParameter("avatarHeight");
        String avatarPaddingValue = request.getParameter("avatarPadding");
        String avatarBlockNumValue = request.getParameter("avatarBlockNum");
        String isNeedMoreMosaic = request.getParameter("isNeedMoreMosaic");

        int avatarWidth = avatarWidthValue != null && !avatarWidthValue.isEmpty() ? Integer.parseInt(avatarWidthValue) : IMG_WIDTH;
        int avatarHeight = avatarHeightValue != null && !avatarHeightValue.isEmpty() ? Integer.parseInt(avatarHeightValue) : IMG_HEIGHT;
        int avatarPadding = avatarPaddingValue != null && !avatarPaddingValue.isEmpty() ? Integer.parseInt(avatarPaddingValue) : PADDING;
        int avatarBlockNum = avatarBlockNumValue != null && !avatarBlockNumValue.isEmpty() ? Integer.parseInt(avatarBlockNumValue) : BLOCK_NUM;

        if (avatarWidth < 0 && avatarHeight < 0 && avatarPadding < 0) {
            AjaxResult ajax = AjaxResult.warn("非法图片参数！");
            return ajax;
        }

        if (!isRightABN(avatarBlockNumValue)) {
            AjaxResult ajax = AjaxResult.warn("每边矩形数量请大于4小于11！");
            return ajax;
        }

        //设置输出文件格式
        response.setContentType("image/png");

        // 得到图片缓冲区
        BufferedImage bi = new BufferedImage(avatarWidth, avatarHeight, BufferedImage.TYPE_INT_RGB);

        // 得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        // 设置背景颜色
        g2.setColor(BACK_GROUND_COLOR);
        g2.fillRect(0, 0, avatarWidth, avatarHeight);

        // 随机颜色
        Color mainColor = getRandomColor();

        // 随机生成有效块坐标集合
        double randomRadioNum;
        if (isNeedMoreMosaic != null && !isNeedMoreMosaic.isEmpty()) {
            randomRadioNum = randomDecimalNumber(isNeedMoreMosaic);
        } else {
            randomRadioNum = randomDecimalNumber("F");
        }
        System.out.println("randomRadioNum:" + randomRadioNum);
        logNowTime();
        System.out.println("========================================");
        List<Point> pointList = getRandomPointList(randomRadioNum, avatarBlockNum);

        // 填充图形
        fillGraph(g2, pointList, mainColor, avatarWidth, avatarHeight, avatarPadding, avatarBlockNum);

        // 获取绘制好的图片的InputStream对象
        InputStream in = getImageStream(bi);
        String base = inputStream2Base64(in);

        // 返回base64信息
        AjaxResult ajax = AjaxResult.success();
        ajax.put("avatar", base);
        return ajax;
    }


    /**
     * 生成随机马赛克图片，保存后返回图片地址信息（暂时不做）
     */
    @RateLimiter(time = 60, count = 20, limitType = LimitType.IP)
    @GetMapping("/getRandomAvatarUrl")
    public AjaxResult getAvatarUrl(HttpServletRequest request) throws Exception {

        String avatarWidthValue = request.getParameter("avatarWidth");
        String avatarHeightValue = request.getParameter("avatarHeight");
        String avatarPaddingValue = request.getParameter("avatarPadding");
        String avatarBlockNumValue = request.getParameter("avatarBlockNum");
        String isNeedMoreMosaic = request.getParameter("isNeedMoreMosaic");

        int avatarWidth = avatarWidthValue != null && !avatarWidthValue.isEmpty() ? Integer.parseInt(avatarWidthValue) : IMG_WIDTH;
        int avatarHeight = avatarHeightValue != null && !avatarHeightValue.isEmpty() ? Integer.parseInt(avatarHeightValue) : IMG_HEIGHT;
        int avatarPadding = avatarPaddingValue != null && !avatarPaddingValue.isEmpty() ? Integer.parseInt(avatarPaddingValue) : PADDING;
        int avatarBlockNum = avatarBlockNumValue != null && !avatarBlockNumValue.isEmpty() ? Integer.parseInt(avatarBlockNumValue) : BLOCK_NUM;

        if (avatarWidth < 0 && avatarHeight < 0 && avatarPadding < 0) {
            AjaxResult ajax = AjaxResult.warn("非法图片参数！");
            return ajax;
        }

        if (!isRightABN(avatarBlockNumValue)) {
            AjaxResult ajax = AjaxResult.warn("每边矩形数量请大于5小于10！");
            return ajax;
        }

        // 得到图片缓冲区
        BufferedImage bi = new BufferedImage(avatarWidth, avatarHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        // 设置背景颜色
        g2.setColor(BACK_GROUND_COLOR);
        g2.fillRect(0, 0, avatarWidth, avatarHeight);

        // 随机颜色
        Color mainColor = getRandomColor();

        // 随机生成有效块坐标集合
        double randomRadioNum;
        if (isNeedMoreMosaic != null && !isNeedMoreMosaic.isEmpty()) {
            randomRadioNum = randomDecimalNumber(isNeedMoreMosaic);
        } else {
            randomRadioNum = randomDecimalNumber("F");
        }
        System.out.println("randomRadioNum:" + randomRadioNum);
        logNowTime();
        System.out.println("========================================");
        List<Point> pointList = getRandomPointList(randomRadioNum, avatarBlockNum);

        // 填充图形
        fillGraph(g2, pointList, mainColor, avatarWidth, avatarHeight, avatarPadding, avatarBlockNum);

        // 通过文件存储流
//        File file = new File(DIR);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        ImageIO.write(bi,"JPG",
//                new FileOutputStream(DIR+System.currentTimeMillis()+".jpg"));

        // 测试返参
        AjaxResult ajax = AjaxResult.success();
        ajax.put("test", "fx67ll");
        return ajax;
    }


    /**
     * 打印当前系统时间
     */
    private static void logNowTime() throws Exception {
        try {
            // 创建日期对象
            Date date = new Date();
            // 创建日期格式化对象，指定格式为 "yyyy-MM-dd HH:mm:ss"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 使用格式化对象将日期对象格式化为字符串，并打印输出
            System.out.println(sdf.format(date));
        } catch (Exception e) {
            throw new Exception("打印当前系统时间操作失败");
        }
    }

    /**
     * 渲染头像并直接返回PNG字节。
     * <p>由调用方负责在带 avatarSeed 的场景缓存结果；图形上下文使用后立即释放，避免频繁翻页时积累本地资源。</p>
     */
    private static byte[] renderAvatar(Random random, int avatarWidth, int avatarHeight,
                                       int avatarPadding, int avatarBlockNum, String mosaicMode)
            throws IOException {
        BufferedImage image = new BufferedImage(avatarWidth, avatarHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setColor(BACK_GROUND_COLOR);
            graphics.fillRect(0, 0, avatarWidth, avatarHeight);
            Color mainColor = getRandomColor(random);
            double randomRadioNum = randomDecimalNumber(mosaicMode, random);
            List<Point> pointList = getRandomPointList(randomRadioNum, avatarBlockNum, random);
            fillGraph(graphics, pointList, mainColor, avatarWidth, avatarHeight,
                    avatarPadding, avatarBlockNum);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (!ImageIO.write(image, "png", outputStream)) {
                throw new IOException("生成头像图片失败");
            }
            return outputStream.toByteArray();
        } finally {
            graphics.dispose();
        }
    }

    /**
     * 解析带上下界的整数参数。非法值回退默认值，越界值收敛到边界。
     */
    private static int parseBoundedInt(String rawValue, int defaultValue, int minValue, int maxValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            int value = Integer.parseInt(rawValue.trim());
            return Math.max(minValue, Math.min(maxValue, value));
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    /**
     * 规范化稳定头像种子，空值表示继续生成一次性随机头像。
     */
    private static String normalizeSeed(String seed) {
        if (seed == null) {
            return null;
        }
        String value = seed.trim();
        if (value.isEmpty()) {
            return null;
        }
        return value.length() > 64 ? value.substring(0, 64) : value;
    }

    /**
     * 马赛克密度只接受 F/Y/N，其他值按随机模式处理。
     */
    private static String normalizeMosaicMode(String mode) {
        if ("Y".equalsIgnoreCase(mode) || "N".equalsIgnoreCase(mode)) {
            return mode.toUpperCase();
        }
        return "F";
    }

    /**
     * 将BufferedImage转为InputStream
     *
     * @param bi 图片文件
     * @return is 文件流
     */
    private static InputStream getImageStream(BufferedImage bi) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (!ImageIO.write(bi, "png", baos)) {
                throw new IOException("图片转流失败");
            }
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            throw new Exception("图片转流失败");
        }
    }

    /**
     * 将inputstream转为Base64
     *
     * @param is 文件流
     * @return String Base64
     * @throws Exception
     */
    private static String inputStream2Base64(InputStream is) throws Exception {
        byte[] data = null;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = is.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new Exception("输入流关闭异常");
                }
            }
        }

        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * 生成一个随机两位小数，范围是0.2-0.85
     *
     * @param isNeedMoreMosaic 需要更多马赛克还是更少，可选值：F/Y/N，F-随机、Y-0.6~0.85之间、N-0.2~0.4之间
     * @return Double
     * @throws Exception
     */
    private static Double randomDecimalNumber(String isNeedMoreMosaic) throws Exception {
        return randomDecimalNumber(isNeedMoreMosaic, ThreadLocalRandom.current());
    }

    /**
     * 按指定随机源生成马赛克密度，避免递归并在稳定种子下得到可复现结果。
     */
    private static double randomDecimalNumber(String isNeedMoreMosaic, Random random) {
        String mode = normalizeMosaicMode(isNeedMoreMosaic);
        for (int attempt = 0; attempt < 32; attempt++) {
            double value = Math.round((random.nextDouble() * 0.65 + 0.2) * 100) / 100.0;
            if ("Y".equals(mode) && value < 0.6) {
                continue;
            }
            if ("N".equals(mode) && value > 0.4) {
                continue;
            }
            return value;
        }
        if ("Y".equals(mode)) {
            return 0.72;
        }
        return "N".equals(mode) ? 0.3 : 0.55;
    }

    /**
     * 校验参数avatarBlockNum是否合法
     *
     * @param avatarBlockNum 马赛克矩形数量
     * @return Boolean
     * @throws Exception
     */
    private static Boolean isRightABN(String avatarBlockNum) throws Exception {
        try {
            if (Integer.parseInt(avatarBlockNum) > 4 && Integer.parseInt(avatarBlockNum) < 11) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("校验参数异常");
        }
    }

    /**
     * 填充图形
     *
     * @param g2        画笔
     * @param pointList 填充块坐标
     * @param mainColor 填充颜色
     */
    private static void fillGraph(Graphics2D g2, List<Point> pointList, Color mainColor, Integer imgWidth, Integer imgHeight, Integer imgPadding, Integer blockNum) {
        int rowBlockLength = (imgWidth - 2 * imgPadding) / blockNum;
        int colBlockLength = (imgHeight - 2 * imgPadding) / blockNum;

        // 填充
        g2.setColor(mainColor);

        // 遍历points
        for (Point point : pointList) {
            g2.fillRect(imgPadding + point.x * rowBlockLength, imgPadding + point.y * colBlockLength, rowBlockLength, colBlockLength);
        }
    }

    /**
     * 获取随机颜色位置列表
     *
     * @param radio 填充色块几率
     * @return List<Point> 列表
     */
    private static List<Point> getRandomPointList(double radio, Integer blockNum) {
        return getRandomPointList(radio, blockNum, ThreadLocalRandom.current());
    }

    /**
     * 使用调用方随机源生成对称马赛克坐标，保证同一 avatarSeed 的图形可复现。
     */
    private static List<Point> getRandomPointList(double radio, Integer blockNum, Random random) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < blockNum / 2; i++) {
            for (int j = 0; j < blockNum; j++) {
                if (random.nextDouble() < radio) {
                    points.add(new Point(i, j));
                }
            }
        }
        addReversePoints(points, blockNum);
        if (blockNum % 2 == 1) {
            for (int i = 0; i < blockNum; i++) {
                if (random.nextDouble() < radio) {
                    points.add(new Point(blockNum / 2, i));
                }
            }
        }
        return points;
    }

    /**
     * 获取随机颜色
     *
     * @return Color对象
     */
    private static Color getRandomColor() {
        return getRandomColor(ThreadLocalRandom.current());
    }

    /**
     * 从指定随机源挑选高辨识度主色。
     */
    private static Color getRandomColor(Random random) {
        for (int attempt = 0; attempt < COLOR_LIMIT; attempt++) {
            int r = random.nextInt(COLOR_LIMIT);
            int g = random.nextInt(COLOR_LIMIT);
            int b = random.nextInt(COLOR_LIMIT);
            if (!evaluateColor(r, g, b, random)) {
                return new Color(r, g, b);
            }
        }
        return new Color(46, 204, 113);
    }

    /**
     * 评价颜色品质，只需任意两种颜色差值大于某个规定值即可
     *
     * @return boolean
     */
    private static boolean evaluateColor(int r, int g, int b) {
        return evaluateColor(r, g, b, ThreadLocalRandom.current());
    }

    /**
     * 评价颜色品质，只需任意两种颜色差值大于随机阈值即可。
     */
    private static boolean evaluateColor(int r, int g, int b, Random random) {
        int rg = Math.abs(r - g);
        int rb = Math.abs(r - b);
        int gb = Math.abs(g - b);
        int max = rg > rb ? (rg > gb ? rg : gb) : (rb > gb ? rb : gb);
        return max < random.nextInt(100) + 1;
    }

    /**
     * 添加对称坐标
     *
     * @param points point的列表
     */
    private static void addReversePoints(List<Point> points, Integer blockNum) {
        ArrayList<Point> pointListCopy = new ArrayList<>(points);
        for (Point point : pointListCopy) {
            points.add(new Point((blockNum - 1 - point.x), point.y));
        }
    }

    /**
     * 封装了坐标的内部类
     */
    private static class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
