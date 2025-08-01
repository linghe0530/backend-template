package com.crane.template.exceptions;

import com.crane.template.enums.ResponseEnum;
import lombok.Getter;

import java.io.Serial;

@Getter
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6564769134768956967L;


    private final Integer code;
    private final String msg;

    public BusinessException() {
        super(ResponseEnum.SYS_ERROR.getDesc());
        this.code = ResponseEnum.SYS_ERROR.getCode();
        this.msg = ResponseEnum.SYS_ERROR.getDesc();
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg) {
        super(msg);
        this.code = ResponseEnum.CUSTOM_ERROR.getCode();
        this.msg = msg;
    }

    public BusinessException(ResponseEnum response) {
        super(response.getDesc());
        this.code = response.getCode();
        this.msg = response.getDesc();
    }

    public BusinessException(Throwable e) {
        super(e.getMessage());
        this.code = 502;
        this.msg = e.getMessage();
    }
}
