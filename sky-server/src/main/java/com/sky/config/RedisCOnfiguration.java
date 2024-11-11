package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisCOnfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("RedisTemplate bean created");

        RedisTemplate redisTemplate = new RedisTemplate();
        // set the connection factory of the redis template
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // set the serializer  of the redis key
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
