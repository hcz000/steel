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
@Schema(name = "InvoiceTitleQueryDTO", description = "发票抬头查询DTO")
public class InvoiceTitleQueryDTO implements Serializable {
    private static final long serialVersionUID = -1238212189701687330L;

    @Query(expression = QueryType.LIKE)
    @Schema(description = "发票抬头名称")
    private String invoiceTitleName;

    @Query(expression = QueryType.BETWEEN)
    @Schema(description = "创建时间")
    private LocalDate[] createTime;

    @QuerySort("createTime")
    @Schema(description = "排序字段")
    private String sortColumn;

    @QueryOrder("desc")
    @Schema(description = "排序方式")
    private String sortOrder;
}
