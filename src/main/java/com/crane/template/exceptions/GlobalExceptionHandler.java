package com.crane.template.exceptions;

import com.crane.template.common.R;
import com.crane.template.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author crane
 * @date 2025.06.27 下午4:52
 * @description
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("参数校验异常:", ex);
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            sb.append(fieldName).append(":").append(errorMessage).append(" ");
        });
        return R.error(sb.toString());
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitException(RateLimitException e) {
        log.warn("触发限流: {}", e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("code", 429);
        response.put("message", e.getMessage());
        response.put("success", false);

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error("系统异常:", e);
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error(ResponseEnum.SYS_ERROR.getDesc(), e);
        return R.error(ResponseEnum.SYS_ERROR.getCode(), ResponseEnum.SYS_ERROR.getDesc());
    }
}
