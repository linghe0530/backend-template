package com.crane.template.model.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.06.25 下午10:22
 * @description
 **/
@Data
@Schema(description = "分页请求参数")
public class PageQuery implements Serializable {
    //改小了前端不会分页
    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE_NUM = 1;

    @Serial
    private static final long serialVersionUID = -4272961948444728170L;
    private static final String DATA_FIELD_NAME_CREATE_TIME = "create_time";
    @Schema(description = "页码", example = "1")
//    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = DEFAULT_PAGE_NUM;

    @Schema(description = "每页大小", example = "5")
    @Min(value = 1, message = "每页查询数量不能小于1")
    private Integer pageSize = DEFAULT_PAGE_SIZE;


    public <T> Page<T> toMpPage(OrderItem... orderItems) {
        Page<T> page = new Page<>(pageNo, pageSize);
        // 是否手动指定排序方式
        if (orderItems != null && orderItems.length > 0) {
            for (OrderItem orderItem : orderItems) {
                page.addOrder(orderItem);
            }
            return page;
        }

        return page;
    }

    public <T> Page<T> toMpPageSortByCreateTimeDesc() {
        return toMpPage(new OrderItem().setColumn(DATA_FIELD_NAME_CREATE_TIME).setAsc(false));
    }

}