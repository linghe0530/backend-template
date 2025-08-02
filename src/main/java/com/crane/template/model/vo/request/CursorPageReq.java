package com.crane.template.model.vo.request;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.template.model.vo.response.CursorPage;
import com.crane.template.utils.CollUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author crane
 * @date 2025.06.25 下午10:22
 * @description
 **/
@Data
@Schema(description = "游标分页请求参数")
public class CursorPageReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -4272961948444728170L;

    public static final Integer DEFAULT_PAGE_SIZE = 5;

    // 如果为null表示没有下一页
    @Schema(description = "下一页游标")
    private Long nextCursor;

    @Schema(description = "每页大小", example = "5")
    @Min(value = 1, message = "每页查询数量不能小于1")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

}