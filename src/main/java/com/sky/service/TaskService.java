package com.sky.service;

import com.sky.entity.Task;
import com.sky.entity.User;
import com.sky.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TaskService {

    /**
     * 获取任务信息
     * @param id
     * @return
     */
    Task findById(Long id);

    boolean mapCheckIn(Long taskId, Double userLatitude, Double userLongitude,Long userId);

    /**
     * 解析任务的经纬度
     * @param
     * @return
     */
    Double[] getTaskLocation(String introduction,int Level);



    /**
     * 解析阅读时长
     * @param taskId
     * @param readingDuration
     * @param userId
     * @return
     */
    boolean readTask(Long taskId, Integer readingDuration, Long userId);


    boolean uploadFile(Long taskId, MultipartFile file);

    void save(Task task);

    List<Task> getAllTasks();
}
