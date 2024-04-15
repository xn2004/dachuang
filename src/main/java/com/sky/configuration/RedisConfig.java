package com.sky.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
















//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//        // Set key serializer
//        template.setKeySerializer(new StringRedisSerializer());
//        // Set value serializer
//        template.setValueSerializer(RedisSerializer.json());
//        // Set hash key serializer
//        template.setHashKeySerializer(new StringRedisSerializer());
//        // Set hash value serializer
//        template.setHashValueSerializer(RedisSerializer.json());
//        // Make sure to initialize the template
//        template.afterPropertiesSet();
//        return template;
//    }
}
