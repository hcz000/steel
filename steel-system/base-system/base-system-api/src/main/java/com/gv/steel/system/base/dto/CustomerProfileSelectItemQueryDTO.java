package com.gv.steel.system.base.dto;

import com.gv.steel.common.mybatis.annotation.query.Query;
import com.gv.steel.common.mybatis.annotation.query.QueryOrder;
import com.gv.steel.common.mybatis.annotation.query.QuerySort;
import com.gv.steel.common.mybatis.annotation.query.SelectColumn;
import com.gv.steel.common.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "CustomerProfileSelectItemQueryDTO", description = "客户档案下拉列表查询DTO")
@SelectColumn({"id", "parentId", "factoryId", "customerType", "customerCode", "customerName", "labelTitle", "printStorageFlag", "paymentTerms"})
public class CustomerProfileSelectItemQueryDTO implements Serializable {
    private static final long serialVersionUID = -5971618047443786596L;

    @Schema(name = "includeDelegateFlag", description = "是否包含委托单位(0-否，1-是)(查询贸易客户时)")
    private Integer includeDelegateFlag = 0;

    @Query(expression = QueryType.EQ)
    @Schema(name = "factoryId", description = "工厂id")
    private Long factoryId;

    @Query(expression = QueryType.EQ)
    @Schema(name = "customerType", description = "客户类型")
    private Integer customerType;

    @Query(expression = QueryType.EQ, column = "parentId")
    @Schema(name = "parentId", description = "委托单位ID")
    private Long customerId;

    @Query(expression = QueryType.EQ)
    @Schema(description = "业务员ID")
    private Long salesmanId;

    @Query(expression = QueryType.EQ)
    @Schema(description = "业务员名称")
    private String salesmanName;

    @QuerySort("customerCode")
    @Schema(name = "sortColumn", description = "排序字段", hidden = true)
    private String sortColumn;

    @QueryOrder("asc")
    @Schema(name = "sortOrder", description = "排序顺序", hidden = true)
    private String sortOrder;
}
