package com.sky.mapper;

import com.sky.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {

    //根据id查询任务信息
    Task findById(Long taskId);

    void insert(Task task);

    void update(Task task);

    List<Task> getAllTasks();
}
