package com.crane.template.aspect;


import com.crane.template.annotation.RateLimit;
import com.crane.template.common.UserContext;
import com.crane.template.exceptions.RateLimitException;
import com.crane.template.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author crane
 * @date 2025.07.04 下午6:38
 * @description 限流切面，实现注解的功能
 **/
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitAspect {

    private final StringRedisTemplate redisTemplate;

    /**
     * Redis Lua脚本，实现原子性的限流逻辑
     */
    private static final String RATE_LIMIT_LUA_SCRIPT =
            "local key = KEYS[1] " +
                    "local count = tonumber(ARGV[1]) " +
                    "local time = tonumber(ARGV[2]) " +
                    "local current = redis.call('get', key) " +
                    "if current and tonumber(current) > count then " +
                    "return tonumber(current) " +
                    "end " +
                    "current = redis.call('incr', key) " +
                    "if tonumber(current) == 1 then " +
                    "redis.call('expire', key, time) " +
                    "end " +
                    "return tonumber(current)";

    private static final DefaultRedisScript<Long> RATE_LIMIT_SCRIPT = new DefaultRedisScript<>();

    static {
        RATE_LIMIT_SCRIPT.setScriptText(RATE_LIMIT_LUA_SCRIPT);
        RATE_LIMIT_SCRIPT.setResultType(Long.class);
    }

    /**
     * SpEL表达式解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 拦截标记了@RateLimit注解的方法
     */
    @Before("@annotation(com.crane.*.annotation.RateLimit) || @within(com.crane.*.annotation.RateLimit)")
    public void rateLimit(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 优先获取方法上的注解，如果没有则获取类上的注解
        RateLimit rateLimit = AnnotationUtils.findAnnotation(method, RateLimit.class);
        if (rateLimit == null) {
            rateLimit = AnnotationUtils.findAnnotation(method.getDeclaringClass(), RateLimit.class);
        }

        if (rateLimit != null) {
            // 执行限流逻辑
            checkRateLimit(point, method, rateLimit);
        }
    }

    /**
     * 执行限流检查
     */
    private void checkRateLimit(JoinPoint point, Method method, RateLimit rateLimit) {
        String key = buildLimitKey(point, method, rateLimit);
        int time = rateLimit.time();
        int count = rateLimit.count();
        TimeUnit timeUnit = rateLimit.timeUnit();

        // 转换为秒
        int seconds = (int) timeUnit.toSeconds(time);
        if (seconds <= 0) {
            seconds = 1;
        }

        try {
            // 执行Redis Lua脚本
            Long current = redisTemplate.execute(
                    RATE_LIMIT_SCRIPT,
                    Collections.singletonList(key),
                    String.valueOf(count),
                    String.valueOf(seconds)
            );

            // 如果当前请求数超过限制，则抛出异常
            if (current != null && current > count) {
                log.warn("触发限流: key={}, count={}/{}, time={}秒", key, current, count, seconds);
                throw new RateLimitException(rateLimit.message());
            }
            log.info("限流检查通过: key={}, count={}/{}, time={}秒", key, current, count, seconds);
        } catch (Exception e) {
            // 如果是限流异常则直接抛出
            if (e instanceof RateLimitException) {
                throw e;
            }

            // 其他异常记录日志但允许请求通过，避免因为限流组件故障影响业务
            log.error("限流检查异常，默认放行: key={}", key, e);
        }
    }

    /**
     * 构建限流的key
     */
    private String buildLimitKey(JoinPoint point, Method method, RateLimit rateLimit) {
        StringBuilder keyBuilder = new StringBuilder("rate_limit:");
        String key = rateLimit.key();

        // 如果没有指定key，则使用类名+方法名
        if (StringUtils.isBlank(key)) {
            key = method.getDeclaringClass().getName() + ":" + method.getName();
        }
        keyBuilder.append(key).append(":");

        // 根据限流类型构建完整的key
        switch (rateLimit.limitType()) {
            case IP:
                keyBuilder.append(getIpAddress());
                break;
            case USER:
                keyBuilder.append(getUserId());
                break;
            case CUSTOM:
                // 使用SpEL表达式解析自定义key
                String customKey = parseSpELExpression(key, point);
                keyBuilder.append(customKey);
                break;
            case DEFAULT:
            default:
                // 默认策略不追加额外信息
                break;
        }

        return keyBuilder.toString();
    }

    /**
     * 获取请求的IP地址
     */
    private String getIpAddress() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 取第一个IP地址
        if (StringUtils.isBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * 获取当前用户ID，可根据实际项目认证体系调整
     */
    private String getUserId() {
        return UserContext.getUser().getUserId();
    }

    /**
     * 获取当前请求对象
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 解析SpEL表达式
     */
    private String parseSpELExpression(String spEL, JoinPoint joinPoint) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = methodSignature.getParameterNames();
            Object[] arguments = joinPoint.getArgs();

            EvaluationContext context = new StandardEvaluationContext();

            // 设置方法参数
            if (paramNames != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    context.setVariable(paramNames[i], arguments[i]);
                }
            }

            // 设置内置变量
            context.setVariable("request", getRequest());
            context.setVariable("ip", getIpAddress());
            context.setVariable("method", methodSignature.getMethod().getName());
            context.setVariable("class", methodSignature.getDeclaringType().getName());
            context.setVariable("args", arguments);

            Expression expression = parser.parseExpression(spEL);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : "null";
        } catch (Exception e) {
            log.error("解析SpEL表达式失败: {}", spEL, e);
            return "spel_error";
        }
    }

}