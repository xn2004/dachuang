package com.sky.service.impl;// TaskServiceImpl.java

import com.sky.entity.Task;
import com.sky.entity.User;
import com.sky.mapper.TaskMapper;
import com.sky.service.TaskService;
import com.sky.service.UserService;
import com.sky.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    // 根据ID查找任务
    public Task findById(Long id) {
        return taskMapper.findById(id);
    }

    @Override
    public boolean mapCheckIn(Long taskId, Double userLatitude, Double userLongitude, Long userId) {
        Task task = findById(taskId);
        if (task == null) {
            log.error("任务不存在，taskId: {}", taskId);
            return false;
        }

        if (task.getLevel() != 101 && task.getLevel() != 201) {
            log.error("非地图签到任务，taskId: {}", taskId);
            return false;
        }

        // 获取任务的经纬度
        Double[] taskLocation = getTaskLocation(task.getIntroduction(), task.getLevel());
        // 获取用户信息
        User user = getUserInfo(userId);
        // 根据任务的经纬度和签到用户的经纬度计算距离
        double distance = calculateDistance(taskLocation[0], taskLocation[1], userLatitude, userLongitude);
        if (distance > 0.1) {
            log.error("签到失败，位置超出范围，taskId: {}", taskId);
            return false;
        }
        // 签到成功，更新任务完成人数和记录已完成用户
        redisUtil.addCompletedUser(taskId, userId, user.getName(), LocalDateTime.now()); // 存入用户ID、姓名和签到时间
        redisUtil.getCompletedUsers(taskId); // 获取已完成任务的用户集合
        return true;
    }


    /**
     * 解析任务的经纬度
     * @param introduction 任务介绍字段
     * @return 经纬度数组，[0]为纬度，[1]为经度
     */
    @Override
    public Double[] getTaskLocation(String introduction, int Level) {
        if (introduction != null) {
            Task task = new Task();
            task.parseIntroduction(introduction, Level); // 传递 Level 参数
            return new Double[]{task.getLatitude(), task.getLongitude()};
        }
        return null;
    }


    /**
     * 计算两个经纬度之间的距离，返回单位为千米
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // 地球半径，单位为千米

        // 将经纬度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 计算经纬度之间的差值
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // 使用 Haversine 公式计算距离
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // 距离，单位为千米

        return distance;
    }



    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    public User getUserInfo(Long userId) {
        return userService.findById(userId);
    }




    @Override
    public boolean readTask(Long taskId, Integer readingDuration, Long userId) {
        Task task = findById(taskId);
        // 获取用户信息
        User user = getUserInfo(userId);
        if (task == null) {
            log.error("任务不存在，taskId: {}", taskId);
            return false;
        }

        // 判断任务类型，如果是阅读型任务（102 或 202），则执行阅读任务逻辑
        if (task.getLevel() == 102 || task.getLevel() == 202) {
            Integer requiredReadingDuration = extractReadingDurationFromIntroduction(task.getIntroduction(), task.getLevel());
            log.info("Task Level: {}, Required Reading Duration: {}, Actual Reading Duration: {}", task.getLevel(), requiredReadingDuration, readingDuration);
            if (requiredReadingDuration != null && readingDuration >= requiredReadingDuration) {
                // 完成阅读任务
                redisUtil.addCompletedUser(taskId, userId, user.getName(), LocalDateTime.now()); // 记录任务已完成用户及完成时间
                redisUtil.getCompletedUsers(taskId); // 获取已完成任务的用户集合
                return true;
            } else {
                // 未完成阅读任务
                return false;
            }
        } else {
            // 非阅读型任务
            log.error("该任务不是阅读型任务，taskId: {}", taskId);
            return false;
        }
    }




    /**
     * 处理文件上传请求
     */
    @Override
    public boolean uploadFile(Long taskId, MultipartFile file) {
        if (file.isEmpty()) {
            log.error("上传文件为空!");
            return false;
        }

        try {
            // 保存文件到服务器指定路径
            String filePath = "/path/to/save/" + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            // 检查数据库中是否已经存在相同ID的任务记录
            Task existingTask = findById(taskId);
            if (existingTask != null) {
                // 如果存在，则更新任务实体的内容
                existingTask.setContent(filePath);
                save(existingTask); // 这里调用的 save 方法应该保证更新操作
                return true;
            } else {
                // 如果不存在，则创建新的任务实体并保存到数据库
                Task newTask = Task.builder()
                        .Id(taskId)
                        .Content(filePath)
                        .build();
                save(newTask); // 这里调用的 save 方法应该保证插入操作
                return true;
            }
        } catch (IOException e) {
            log.error("文件上传失败: " + e.getMessage());
            return false;
        }
    }




    /**
     * 获取解析后的阅读时长
     * @param introduction
     * @return
     */
    private Integer extractReadingDurationFromIntroduction(String introduction, int Level) {
        if (introduction != null) {
            Task task = new Task();
            task.parseIntroduction(introduction, Level); // 使用传入的 Level 参数
            return task.getReadingDuration();
        }
        return null;
    }

    @Override
    public void save(Task task) {
        if (task.getId() == null) {
            // 如果任务ID为空，则执行插入操作
            taskMapper.insert(task);
        } else {
            // 如果任务ID不为空，则执行更新操作
            taskMapper.update(task);
        }
    }


    @Override
    public List<Task> getAllTasks() {
        return taskMapper.getAllTasks();
    }
}
