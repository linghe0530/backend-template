package com.crane.template.model.vo.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.crane.template.utils.CollUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author crane
 * @date 2025.06.29 上午1:14
 * @description
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页结果")
public class PageResp<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8331885987484720355L;
    @Schema(description = "当前页码")
    protected Long pageNo;
    @Schema(description = "一页多少条数据")
    protected Long pageSize;
    @Schema(description = "总条数")
    protected Long total;
    @Schema(description = "总页码数")
    protected Long pages;
    @Schema(description = "当前页数据")
    protected List<T> list;


    public static <T> PageResp<T> empty(Long total, Long pageSize, Long pageNo, Long pages) {
        return new PageResp<>(pageNo, pageSize, total, pages, CollUtils.emptyList());
    }

    public static <T> PageResp<T> empty(Page<?> page) {
        return new PageResp<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), CollUtils.emptyList());
    }

    public static <T> PageResp<T> of(Page<T> page) {
        if (page == null) {
            return new PageResp<>();
        }
        if (CollUtils.isEmpty(page.getRecords())) {
            return empty(page);
        }
        return new PageResp<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), page.getRecords());
    }


}
