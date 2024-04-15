package com.sky.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET})
@RestController
public class AduploadController {

    @GetMapping("/adupload")
    public ResponseEntity<String> downloadFile() throws IOException {
        // 读取文件
        Path path = Paths.get("D:/西安邮电大学工作文件/2.jpg");

        // 如果文件不存在，返回404
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        // 将文件读取为字节数组
        byte[] data = Files.readAllBytes(path);

        // 将文件内容转换为 Base64 编码的字符串
        String base64Image = Base64.getEncoder().encodeToString(data);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // 设置响应内容类型为 JSON，可根据实际情况修改
        headers.setContentDispositionFormData("attachment", path.getFileName().toString()); // 设置文件名

        // 返回 Base64 编码的图片字符串给前端
        return ResponseEntity.ok()
                .headers(headers)
                .body(base64Image);
    }
}
