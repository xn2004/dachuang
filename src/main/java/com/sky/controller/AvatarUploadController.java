package com.sky.controller;

import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@CrossOrigin(origins = "*",methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
@RestController
@Slf4j
public class AvatarUploadController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/avatar/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("userId") Long userId) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return "上传的文件为空";
        }

        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 将文件转换为Base64编码的字符串
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            String filePath = "D:/西安邮电大学工作文件" + fileName;
            File dest = new File(filePath);
            file.transferTo(dest);
            // 更新用户头像路径到数据库
            userService.updateUserAvatar(userId, filePath);
            log.info(base64Image);
            // 返回成功响应
            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
            // 返回失败响应
            return "头像上传失败";
        }
    }
    }


/**
 * String fileName = file.getOriginalFilename();
 *                 // 获取文件存储路径（这里假设存储在本地）
 *                 String filePath = "D:/西安邮电大学工作文件" + fileName;
 *                 // 创建文件对象
 *                 File dest = new File(filePath);
 *                 // 将上传的文件保存到目标路径
 *                 file.transferTo(dest);
 *
 *                 // 更新用户头像路径到数据库
 *                 userService.updateUserAvatar(userId, filePath);
 */