package com.ruoyi.web.controller.common;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

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
     * 保存路径
     */
//    private final static String DIR = "D://headImg/";

    /**
     * 直接返回随机马赛克图片流
     */
    @GetMapping("/getRandomAvatar")
    public void getAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String avatarWidthValue = request.getParameter("avatarWidth");
        String avatarHeightValue = request.getParameter("avatarHeight");
        String avatarPaddingValue = request.getParameter("avatarPadding");
        String avatarBlockNumValue = request.getParameter("avatarBlockNum");
        String isNeedMoreMosaic = request.getParameter("isNeedMoreMosaic");

        int avatarWidth = avatarWidthValue != null && !avatarWidthValue.isEmpty() ? Integer.parseInt(avatarWidthValue) : IMG_WIDTH;
        int avatarHeight = avatarHeightValue != null && !avatarHeightValue.isEmpty() ? Integer.parseInt(avatarHeightValue) : IMG_HEIGHT;
        int avatarPadding = avatarPaddingValue != null && !avatarPaddingValue.isEmpty() ? Integer.parseInt(avatarPaddingValue) : PADDING;
        int avatarBlockNum = avatarBlockNumValue != null && !avatarBlockNumValue.isEmpty() ? Integer.parseInt(avatarBlockNumValue) : BLOCK_NUM;
        String isNeedMoreMosaicDefault = isNeedMoreMosaic != null && !isNeedMoreMosaic.isEmpty() ? isNeedMoreMosaic : "F";

        // 设置输出文件格式
        response.setContentType("image/png");

        // 得到图片缓冲区
        BufferedImage bi = new BufferedImage
                (avatarWidth, avatarHeight, BufferedImage.TYPE_INT_RGB);

        // 得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        // 设置背景颜色
        g2.setColor(BACK_GROUND_COLOR);
        g2.fillRect(0, 0, avatarWidth, avatarHeight);

        // 随机颜色
        Color mainColor = getRandomColor();

        // 随机生成有效块坐标集合
        double randomRadioNum = 0;
        try {
            randomRadioNum = randomDecimalNumber(isNeedMoreMosaicDefault);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("randomRadioNum:" + randomRadioNum);
        System.out.println("========================================");
        List<Point> pointList = getRandomPointList(randomRadioNum, avatarBlockNum);

        // 填充图形
        fillGraph(g2, pointList, mainColor, avatarWidth, avatarHeight, avatarPadding, avatarBlockNum);

        // 下面都是关于流处理的流程
        // 获取绘制好的图片的InputStream对象
        InputStream in = null;
        try {
            in = getImageStream(bi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        // 创建存放文件内容的数组
        byte[] buff = new byte[1024];
        // 所读取的内容使用n来接收
        int n;
        // 当没有读取完时,继续读取,循环
        while ((n = in.read(buff)) != -1) {
            // 将字节数组的数据全部写入到输出流中
            outputStream.write(buff, 0, n);
        }
        // 强制将缓存区的数据进行输出
        outputStream.flush();
        // 关流
        outputStream.close();
        in.close();
    }

    /**
     * 返回随机马赛克图片的Base64
     */
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
        BufferedImage bi = new BufferedImage
                (avatarWidth, avatarHeight, BufferedImage.TYPE_INT_RGB);

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
        BufferedImage bi = new BufferedImage
                (avatarWidth, avatarHeight, BufferedImage.TYPE_INT_RGB);

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
     * 将BufferedImage转为InputStream
     *
     * @param bi 图片文件
     * @return is 文件流
     */
    private static InputStream getImageStream(BufferedImage bi) throws Exception {
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream imos;
        try {
            imos = ImageIO.createImageOutputStream(baos);
            ImageIO.write(bi, "png", imos);
            is = new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            throw new Exception("图片转流失败");
        }
        return is;
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
        try {
            Random random = new Random();
            Double randomDecimal = random.nextDouble() * 0.65 + 0.2;
            Double lowDecimal = new Double("0.4");
            Double highDecimal = new Double("0.6");
            randomDecimal = Math.round(randomDecimal * 100) / 100.0;
            if (isNeedMoreMosaic.equals("Y") && randomDecimal.compareTo(highDecimal) < 0) {
                return randomDecimalNumber(isNeedMoreMosaic);
            } else if (isNeedMoreMosaic.equals("N") && randomDecimal.compareTo(lowDecimal) > 0) {
                return randomDecimalNumber(isNeedMoreMosaic);
            } else {
                return randomDecimal;
            }
        } catch (Exception e) {
            throw new Exception("获取随机数异常");
        }
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
            g2.fillRect(imgPadding + point.x * rowBlockLength,
                    imgPadding + point.y * colBlockLength,
                    rowBlockLength, colBlockLength);
        }
    }

    /**
     * 获取随机颜色位置列表
     *
     * @param radio 填充色块几率
     * @return List<Point> 列表
     */
    private static List<Point> getRandomPointList(double radio, Integer blockNum) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < blockNum / 2; i++) {
            for (int j = 0; j < blockNum; j++) {
                if (Math.random() < radio) {
                    points.add(new Point(i, j));
                }
            }
        }
        addReversePoints(points, blockNum);
        if (blockNum % 2 == 1) {
            for (int i = 0; i < blockNum; i++) {
                if (Math.random() < radio) {
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
        int r, g, b;
        do {
            r = new Random().nextInt(COLOR_LIMIT);
            g = new Random().nextInt(COLOR_LIMIT);
            b = new Random().nextInt(COLOR_LIMIT);
        } while (evaluateColor(r, g, b));
        return new Color(r, g, b);
    }

    /**
     * 评价颜色品质，只需任意两种颜色差值大于某个规定值即可
     *
     * @return boolean
     */
    private static boolean evaluateColor(int r, int g, int b) {
        int rg = Math.abs(r - g);
        int rb = Math.abs(r - b);
        int gb = Math.abs(g - b);
        int max = rg > rb ? (rg > gb ? rg : gb) : (rb > gb ? rb : gb);
        int randomColorNum = new Random().nextInt(100) + 1;
        System.out.println("randomColorNum:" + randomColorNum);
        return max < randomColorNum;
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
