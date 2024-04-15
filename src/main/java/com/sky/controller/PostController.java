package com.sky.controller;
import com.sky.entity.Post;
import com.sky.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
@CrossOrigin(origins = "*",methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @PostMapping("/publish")
    public String publishPost(@RequestBody Post post)  {// 将照片信息设置到帖子对象中
        postService.publishPost(post);
        return "发布成功!";
    }


    @PostMapping("/publish/images")
    public String uploadFile(@RequestParam("photo") MultipartFile file,
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
            postService.updateImage(userId, filePath); // 直接调用实例方法
            log.info(base64Image);
            // 返回成功响应
            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
            // 返回失败响应
            return "图片上传失败";
        }
    }

    /**
     * 查看某人主页帖子
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable Long userId) {
        return postService.getPostsByUserId(userId);
    }

    /**
     * 广场帖子
     * @return
     */
    @GetMapping("/allposts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
}
