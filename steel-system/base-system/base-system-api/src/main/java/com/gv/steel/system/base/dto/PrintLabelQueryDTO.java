package com.gv.steel.system.base.dto;

import com.gv.steel.common.mybatis.annotation.query.Query;
import com.gv.steel.common.mybatis.annotation.query.QueryOrder;
import com.gv.steel.common.mybatis.annotation.query.QuerySort;
import com.gv.steel.common.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(name = "PrintLabelQueryDTO", description = "打印标签查询DTO")
public class PrintLabelQueryDTO implements Serializable {
    private static final long serialVersionUID = 7925048925538506330L;

    @Query(expression = QueryType.LIKE)
    @Schema(description = "标签名称")
    private String labelName;

    @Query(expression = QueryType.EQ)
    @Schema(description = "标签类型(0-标签，1-其他)")
    private String labelType;

    @Query(expression = QueryType.BETWEEN)
    @Schema(description = "创建时间")
    private LocalDate[] createTime;

	@QuerySort(value = "orderSort, createTime")
    @Schema(description = "排序字段", hidden = true)
    private String sortColumn;

    @QueryOrder(value = "asc")
    @Schema(description = "排序顺序", hidden = true)
    private String sortOrder;
}
