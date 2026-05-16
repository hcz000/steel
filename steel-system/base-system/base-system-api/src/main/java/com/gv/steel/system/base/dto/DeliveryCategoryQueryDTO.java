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
@Schema(name = "DeliveryCategoryQueryDTO", description = "出货单类型查询DTO")
public class DeliveryCategoryQueryDTO implements Serializable {
    private static final long serialVersionUID = 5399343638941999674L;

    @Schema(description = "出货单类型名称")
    @Query(expression = QueryType.LIKE)
    private String deliveryOrderName;

    @Schema(description = "出货单类型创建时间")
    @Query(expression = QueryType.BETWEEN)
    private LocalDate[] createTime;

    @Schema(description = "排序字段", hidden = true)
    @QuerySort(value = "createTime")
    private String sortColumn;

    @Schema(description = "排序顺序", hidden = true)
    @QueryOrder(value = "desc")
    private String sortOrder;
}
