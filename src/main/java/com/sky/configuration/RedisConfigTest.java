package com.sky.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.redis.core.RedisTemplate;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class RedisConfigTest {

    @Autowired(required = false)
    private RedisTemplate redisTemplate;

//    @Test
    public void getName() {
        redisTemplate.opsForValue().set("name","dadadingdada");
        System.out.println(redisTemplate.opsForValue().get("name"));

    }
}
