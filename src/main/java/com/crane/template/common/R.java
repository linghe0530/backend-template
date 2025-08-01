package com.crane.template.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "通用响应结果")
public class R<T> {
    @Schema(description = "业务状态码，200-成功，其它-失败")
    private int code;
    @Schema(description = "响应消息", example = "OK")
    private String msg;
    @Schema(description = "响应数据")
    private T data;

    public static <T> R<T> ok() {
        return new R<>(200, "success", null);
    }


    public static <T> R<T> ok(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(501, msg, null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


}
