package com.sky.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 记录任务已完成用户和完成时间
    public void addCompletedUser(Long taskId, Long userId, String userName, LocalDateTime completionTime) {
        String hashKey = "SL:" + taskId + ":completedUsers";
        String completionInfo = userName + ":" + completionTime.toString();
        redisTemplate.opsForHash().put(hashKey, String.valueOf(userId), completionInfo);
        // 打印日志
        log.info("Added completed user: taskId={}, userId={}, completionInfo={}", taskId, userId, completionInfo);
    }

    // 获取任务已完成用户及完成时间
    public Map<Object, Object> getCompletedUsers(Long taskId) {
        String hashKey = "SL:" + taskId + ":completedUsers";
        return redisTemplate.opsForHash().entries(hashKey);
    }

}
