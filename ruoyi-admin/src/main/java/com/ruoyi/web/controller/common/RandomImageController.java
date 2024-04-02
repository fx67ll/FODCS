package com.ruoyi.web.controller.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

@RestController
public class RandomImageController {

    private static final String IMAGE_DIRECTORY_PATH = "/home/ruoyi/randomImages";

    @GetMapping(value = "/getRandomImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getRandomImage(HttpServletRequest request) {

        String imgUrlSuffix = (request.getParameter("imgUrlSuffix") != null && !request.getParameter("imgUrlSuffix").isEmpty()) ? request.getParameter("imgUrlSuffix") : "/forBlog";
        System.out.println(imgUrlSuffix);

        // 打印一下请求的ip地址
        String clientIpAddress = request.getRemoteAddr();
        System.out.println("getRandomImage API -> Client IP Address: " + clientIpAddress);

        File imageDirectory = new File(IMAGE_DIRECTORY_PATH + imgUrlSuffix);
        File[] images = imageDirectory.listFiles();

        if (images != null && images.length > 0) {
            Random random = new Random();
            int randomIndex = random.nextInt(images.length);
            File randomImage = images[randomIndex];

            try (FileInputStream fis = new FileInputStream(randomImage)) {
                byte[] imageBytes = new byte[(int) randomImage.length()];
                fis.read(imageBytes);
                return ResponseEntity.ok().body(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
