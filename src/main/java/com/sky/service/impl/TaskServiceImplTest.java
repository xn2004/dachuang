//package com.sky.service.impl;
//
//import com.sky.entity.Task;
//import com.sky.mapper.TaskMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class TaskServiceImplTest {
//
//    @Autowired
//    private TaskServiceImpl taskService;
//
//    @MockBean
//    private TaskMapper taskMapper;
//
//    @MockBean
//    private RedisTemplate<String, String> redisTemplate;
//
//    @BeforeEach
//    public void setUp() {
//        // 这里不需要初始化 Mockito，SpringBootTest 已经处理了
//    }
//
//    @Test
//    public void testCheckIn_Success() {
//        // 准备
//        Long taskId = 1L;
//        Task task = new Task();
//        task.setLevel(101);
//        task.setLatitude(40.7128); // 纽约市的纬度
//        task.setLongitude(-74.0060); // 纽约市的经度
//
//        when(taskMapper.findById(taskId)).thenReturn(task);
//
//        // 执行
//        taskService.checkIn(taskId);
//
//        // 断言
//        verify(taskMapper, times(1)).findById(taskId);
//        verify(redisTemplate, times(1)).opsForZSet();
//        // 根据需要添加更多断言
//    }
//
//    @Test
//    public void testCompleteReadingTask_Success() {
//        // 准备
//        Long taskId = 2L;
//        Task task = new Task();
//        task.setLevel(102);
//        task.setIntroduction("这是一个用于测试的示例任务。");
//
//        when(taskMapper.findById(taskId)).thenReturn(task);
//
//        // 执行
//        taskService.completeReadingTask(taskId);
//
//        // 断言
//        verify(taskMapper, times(1)).findById(taskId);
//        verify(redisTemplate, times(1)).opsForZSet();
//        // 根据需要添加更多断言
//    }
//
//    // 根据不同情况添加更多测试方法
//}
