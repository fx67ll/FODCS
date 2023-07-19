package com.ruoyi.web.controller.common;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
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
     */
    private final static int IMG_WIDTH = 360;

    /**
     * 图片高
     */
    private final static int IMG_HEIGHT = 360;

    /**
     * 图片边缘内边距
     */
    private final static int PADDING = 30;

    /**
     * 填充比率，越接近1，有色色块出现几率越高
     */
//    private final static double RADIO = 0.45;

    /**
     * 每边矩形数量（建议>=5）
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
    public void getAvatar(HttpServletResponse response) throws Exception {

        //设置输出文件格式
        response.setContentType("image/png");

        // 得到图片缓冲区
        BufferedImage bi = new BufferedImage
                (IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        // 设置背景颜色
        g2.setColor(BACK_GROUND_COLOR);
        g2.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);
        // 随机颜色
        Color mainColor = getRandomColor();
        // 随机生成有效块坐标集合
        double randomRadioNum = randomDecimalNumber();
        System.out.println("randomRadioNum:" + randomRadioNum);
        List<Point> pointList = getRandomPointList(randomRadioNum);
        // 填充图形
        fillGraph(g2, pointList, mainColor);

        // 获取绘制好的图片的InputStream对象
        InputStream in = getImageStream(bi);
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
    public AjaxResult getAvatarBase64(HttpServletResponse response) throws Exception {

        //设置输出文件格式
        response.setContentType("image/png");

        // 得到图片缓冲区
        BufferedImage bi = new BufferedImage
                (IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        // 设置背景颜色
        g2.setColor(BACK_GROUND_COLOR);
        g2.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);
        // 随机颜色
        Color mainColor = getRandomColor();
        // 随机生成有效块坐标集合
        double randomRadioNum = randomDecimalNumber();
        System.out.println("randomRadioNum:" + randomRadioNum);
        List<Point> pointList = getRandomPointList(randomRadioNum);
        // 填充图形
        fillGraph(g2, pointList, mainColor);

        // 获取绘制好的图片的InputStream对象
        InputStream in = getImageStream(bi);
        String base = inputStream2Base64(in);

        AjaxResult ajax = AjaxResult.success();
        ajax.put("avatar", base);
        return ajax;
    }


    /**
     * 生成随机马赛克图片，保存后返回图片地址信息（暂时不做）
     */
    @GetMapping("/getRandomAvatarUrl")
    public AjaxResult getAvatarUrl(HttpServletResponse response) throws Exception {
//    public AjaxResult getAvatarUrl(HttpServletResponse response) throws IOException {

        // 得到图片缓冲区
        BufferedImage bi = new BufferedImage
                (IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // 得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        // 设置背景颜色
        g2.setColor(BACK_GROUND_COLOR);
        g2.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);

        // 随机颜色
        Color mainColor = getRandomColor();

        // 随机生成有效块坐标集合
        double randomRadioNum = randomDecimalNumber();
        System.out.println("randomRadioNum:" + randomRadioNum);
        List<Point> pointList = getRandomPointList(randomRadioNum);

        // 填充图形
        fillGraph(g2, pointList, mainColor);

//        File file = new File(DIR);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        ImageIO.write(bi,"JPG",
//                new FileOutputStream(DIR+System.currentTimeMillis()+".jpg"));

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
    private static InputStream getImageStream(BufferedImage bi) {
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream imos;
        try {
            imos = ImageIO.createImageOutputStream(baos);
            ImageIO.write(bi, "png", imos);
            is = new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
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
     * 生成一个随机两位小数，范围是0.25-0.75
     *
     * @return Double
     * @throws Exception
     */
    private static Double randomDecimalNumber() throws Exception {
        Random random = new Random();
        double randomDecimal = random.nextDouble() * 0.5 + 0.25;
        randomDecimal = Math.round(randomDecimal * 100) / 100.0;
        return randomDecimal;
//        return Math.round((new Random().nextDouble() * 0.75 + 0.25) * 100) / 100.0;
    }


    /**
     * 填充图形
     *
     * @param g2        画笔
     * @param pointList 填充块坐标
     * @param mainColor 填充颜色
     */
    private static void fillGraph(Graphics2D g2, List<Point> pointList, Color mainColor) {
        int rowBlockLength = (IMG_HEIGHT - 2 * PADDING) / BLOCK_NUM;
        int colBlockLength = (IMG_WIDTH - 2 * PADDING) / BLOCK_NUM;
        // 填充
        g2.setColor(mainColor);
        // 遍历points
        for (Point point : pointList) {
            g2.fillRect(PADDING + point.x * rowBlockLength,
                    PADDING + point.y * colBlockLength,
                    rowBlockLength, colBlockLength);
        }
    }

    /**
     * 获取随机颜色位置列表
     *
     * @param radio 填充色块几率
     * @return List<Point> 列表
     */
    private static List<Point> getRandomPointList(double radio) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < BLOCK_NUM / 2; i++) {
            for (int j = 0; j < BLOCK_NUM; j++) {
                if (Math.random() < radio) {
                    points.add(new Point(i, j));
                }
            }
        }
        addReversePoints(points);
        if (BLOCK_NUM % 2 == 1) {
            for (int i = 0; i < BLOCK_NUM; i++) {
                if (Math.random() < radio) {
                    points.add(new Point(BLOCK_NUM / 2, i));
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
    private static void addReversePoints(List<Point> points) {
        ArrayList<Point> pointListCopy = new ArrayList<>(points);
        for (Point point : pointListCopy) {
            points.add(new Point((BLOCK_NUM - 1 - point.x), point.y));
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
