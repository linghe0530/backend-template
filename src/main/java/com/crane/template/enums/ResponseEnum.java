package com.crane.template.enums;

import lombok.Getter;

/**
 * @author crane
 * @date 2025.06.29 上午12:57
 * @description
 **/
@Getter
public enum ResponseEnum {
    CUSTOM_ERROR(1001, "自定义异常信息"),
    SYS_ERROR(1002, "系统开小差了,请联系管理员"),
    NOT_LOGIN(1004, "登录失效,请重新登录"),
    NO_AUTH(1005, "暂无权限访问"),
    NO_RESOURCE(1006, "未找到该资源");

    private final Integer code;
    private final String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
