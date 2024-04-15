package com.sky.controller;

import com.sky.entity.Task;
import com.sky.result.Result;
import com.sky.service.TaskService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*",methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
@RestController
@RequestMapping("/tasks")
@Api(tags = "用户任务相关接口")
@Slf4j
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * 处理地图签到请求
     *
     * @param taskId
     * @param userLongitude
     * @param userLatitude
     * @param userId
     * @return
     */
    @GetMapping("/mapCheckIn")
    public String mapCheckIn(@RequestParam("taskId") Long taskId,
                             @RequestParam("userLongitude") Double userLongitude,
                             @RequestParam("userLatitude") Double userLatitude,
                             @RequestParam("userId") Long userId) {
        boolean success = taskService.mapCheckIn(taskId, userLatitude, userLongitude, userId);
        if (success) {
            return "签到成功!";
        } else {
            return "签到失败!不在指定范围内";
        }
    }


    /**
     * 处理阅读任务请求
     */

    @GetMapping("/readTask")
    public String readTask(@RequestParam("taskId") Long taskId,
                           @RequestParam("readingDuration") Integer readingDuration,
                           @RequestParam("userId") Long userId) {
        boolean success = taskService.readTask(taskId, readingDuration, userId);
        if (success) {
            return "阅读任务完成!";
        } else {
            return "阅读任务未完成!";
        }
    }




    @GetMapping("/uploadFile")
    public ResponseEntity<byte[]> getFile(@RequestParam("taskId") Long taskId) {
        try {
            // 构造文件路径
            String filePath = "D:/西安邮电大学工作文件/task_" + taskId + ".txt";

            // 检查文件是否存在
            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("文件不存在!".getBytes());
            }

            // 读取文件内容
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

            // 构建响应头部
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "task_" + taskId + ".txt");
            headers.setContentLength(fileContent.length);

            // 返回文件内容和响应头部
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("读取文件失败!".getBytes());
        }
    }

    @GetMapping("/getAllTasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
}




















