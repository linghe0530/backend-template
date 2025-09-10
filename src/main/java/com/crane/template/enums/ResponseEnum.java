package com.crane.template.enums;

import lombok.Getter;

/**
 * @author crane
 * @date 2025.06.29 上午12:57
 * @description
 **/
@Getter
public enum ResponseEnum {

    NO_RESOURCE(1006, "未找到该资源"),
    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "暂无权限访问"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYS_ERROR(50000, "系统开小差了,请联系管理员"),
    OPERATION_ERROR(50001, "操作失败"),
    CUSTOM_ERROR(50002, "自定义异常信息");

    private final Integer code;
    private final String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
