//package com.crane.template.config;
//
//import com.crane.meeting.constants.MeetingConstants;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author crane
// * @date 2025.06.29 下午5:29
// * @description
// **/
//@Configuration
//@ConditionalOnProperty(name = MeetingConstants.MESSAGEING_HANDLE_CHANNEL_KEY, havingValue = MeetingConstants.MESSAGEING_HANDLE_CHANNEL_REDIS)
//@Slf4j
//public class RedissonConfig {
//    @Resource
//    private RedisProperties redisProperties;
//
//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        log.info("redis://{}:{}", redisProperties.getHost(), redisProperties.getPort());
//        config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
//                .setPassword(redisProperties.getPassword());
//        return Redisson.create(config);
//    }
//
//}
