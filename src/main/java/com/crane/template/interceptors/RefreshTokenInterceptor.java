package com.crane.template.interceptors;

import com.crane.template.common.UserContext;
import com.crane.template.utils.RedisUtils;
import com.crane.template.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author crane
 * @date 2025.06.28 下午11:48
 * @description
 **/
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final RedisUtils redisUtils;

    public RefreshTokenInterceptor(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return true;
        }
        //todo token校验
//        TokenUserInfo userInfo = redisUtils.getTokenUserInfo(token);
//        if (userInfo == null) {
//            return true;
//        }
//        UserContext.setUser(userInfo);
//        //剩余三小时自动刷新
//        if (userInfo.getExpireTime().getTime() - System.currentTimeMillis() <= 1000 * 3600 * 3) {
//            redisUtils.refreshToken(token,userInfo);
//        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
