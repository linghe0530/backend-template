package com.crane.template.config;

import com.crane.meeting.interceptors.LoginInterceptor;
import com.crane.meeting.interceptors.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author crane
 * @date 2025.06.29 上午12:39
 * @description
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private RefreshTokenInterceptor refreshTokenInterceptor;
    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refreshTokenInterceptor);
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/login",
                        "/v2/**",
                        "/v3/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/doc.html"
                );
    }
}
