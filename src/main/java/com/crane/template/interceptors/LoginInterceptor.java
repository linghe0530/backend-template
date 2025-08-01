package com.crane.template.interceptors;

import com.crane.template.common.TokenUserInfo;
import com.crane.template.common.UserContext;
import com.crane.template.enums.ResponseEnum;
import com.crane.template.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author crane
 * @date 2025.06.28 下午11:47
 * @description
 **/
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true; // 静态资源等非方法处理器直接放行
        }
        TokenUserInfo userInfo = UserContext.getUser();
        if (userInfo == null) {
            WebUtils.renderError(request, response, ResponseEnum.NOT_LOGIN.getCode(), ResponseEnum.NOT_LOGIN.getDesc());
            return false;
        }

        return false;
    }


}
