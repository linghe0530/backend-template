package com.crane.template.model.vo.response;

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
@Schema(description = "游标分页结果")
public class CursorPage<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8331885987484720355L;

    @Schema(description = "当前页数据")
    private List<T> list;
    // 如果为null表示没有下一页
    @Schema(description = "下一页游标")
    private Long nextCursor;
    @Schema(description = "是否有下一页")
    private Boolean hasNext;


    public static <T> CursorPage<T> empty(Long nextCursor, Boolean hasNext) {
        return new CursorPage<>(CollUtils.emptyList(), nextCursor, hasNext);
    }

    public static <T> CursorPage<T> of(Long nextCursor, Boolean hasNext, List<T> list) {
        return new CursorPage<>(list, nextCursor, hasNext);
    }
}
