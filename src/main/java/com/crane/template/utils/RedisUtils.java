package com.crane.template.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @author crane
 * @date 2025.06.25 下午11:20
 * @description
 **/
@Component
public class RedisUtils {

    @Resource
    private StringRedisTemplate stringRedisTemplate;



}
