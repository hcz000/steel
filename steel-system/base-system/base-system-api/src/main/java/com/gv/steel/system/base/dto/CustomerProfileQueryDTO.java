package com.gv.steel.system.base.dto;

import com.gv.steel.common.mybatis.annotation.query.Query;
import com.gv.steel.common.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(name = "CustomerProfileQueryDTO", description = "客户档案查询DTO")
public class CustomerProfileQueryDTO implements Serializable {
    private static final long serialVersionUID = -2812255279230956207L;

    @Query(expression = QueryType.EQ)
    @Schema(description = "加工厂ID")
    private Long factoryId;

    @Query(expression = QueryType.EQ)
    @Schema(description = "委托客户ID")
    private Long customerId;

    @Query(expression = QueryType.LIKE)
    @Schema(description = "客户代码")
    private String customerCode;

    @Query(expression = QueryType.LIKE)
    @Schema(description = "客户名称")
    private String customerName;

    @Query(expression = QueryType.BETWEEN)
    @Schema(description = "创建时间")
    private LocalDate[] createTime;
}
