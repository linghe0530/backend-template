package com.crane.template.annotation;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author crane
 * @date 2025.07.04 下午6:37
 * @description 限流注解，用于接口限流控制
 * 可应用于类或方法上，方法上的配置优先于类上的配置
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流唯一标识，用于区分不同的限流策略
     * 为空时会使用类名+方法名作为key
     */
    String key() default "";

    /**
     * 限流时间窗口，默认1
     */
    int time() default 1;

    /**
     * 时间单位，默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流时间窗口内最大请求数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;

    /**
     * 限流后的提示信息
     */
    String message() default "请求过于频繁，请稍后再试";


    /**
     * 限流类型枚举
     */
    enum LimitType {
        /**
         * 默认策略，针对接口维度限流
         */
        DEFAULT,

        /**
         * 根据请求IP限流
         */
        IP,

        /**
         * 根据用户ID限流
         */
        USER,

        /**
         * 自定义限流键，需要通过SpEL表达式提供
         */
        CUSTOM
    }
}
