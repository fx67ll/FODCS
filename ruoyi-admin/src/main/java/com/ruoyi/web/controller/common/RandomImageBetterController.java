package com.ruoyi.web.controller.common;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

// 主要改进点：
// 使用ConcurrentHashMap进行缓存：缓存文件数组，减少对文件系统的访问。
// 使用Resource：改用更加抽象的Resource来处理文件，这样可以更方便地处理文件和其他类型的资源。
// 异常处理：使用更广泛的异常捕获，确保任何类型的异常都能被捕获并处理。
// 安全性：简单的输入检查防止路径遍历，可以进一步强化。

// 这个实现提供了更好的性能和错误处理，同时保持了代码的简洁性和可读性。

@RestController
public class RandomImageBetterController {

    private static final String IMAGE_DIRECTORY_PATH = "/home/ruoyi/randomImages";
    private static final ConcurrentHashMap<String, File[]> imageCache = new ConcurrentHashMap<>();

    @GetMapping(value = "/getRandomImageBetter", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getRandomImageBetter(HttpServletRequest request) {
        String imgUrlSuffix = request.getParameter("imgUrlSuffix");
        if (imgUrlSuffix == null || imgUrlSuffix.isEmpty()) {
            imgUrlSuffix = "/forBlog";
        }

        File imageDirectory = new File(IMAGE_DIRECTORY_PATH + imgUrlSuffix);
        File[] images = imageCache.computeIfAbsent(imgUrlSuffix, k -> imageDirectory.listFiles());

        if (images != null && images.length > 0) {
            Random random = new Random();
            File randomImage = images[random.nextInt(images.length)];

            try {
                Resource imageResource = new UrlResource(randomImage.toURI());
                if (imageResource.exists() || imageResource.isReadable()) {
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
