package com.crane.template.exceptions;



import java.io.Serial;

/**
 * @author crane
 * @date 2025.07.04 下午6:49
 * @description 限流异常
 **/
public class RateLimitException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1398571780294166659L;

    public RateLimitException(String message) {
        super(message);
    }

    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}