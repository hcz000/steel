package com.gv.steel.system.user.dto;

import com.gv.steel.common.mybatis.annotation.query.Query;
import com.gv.steel.common.mybatis.annotation.query.QueryOrder;
import com.gv.steel.common.mybatis.annotation.query.QuerySort;
import com.gv.steel.common.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "sysRoleQueryDTO", description = "查询角色信息DTO")
public class SysRoleQueryDTO implements Serializable {
    private static final long serialVersionUID = -5048893919160692112L;

    @Schema(description = "角色名称")
    @Query(expression = QueryType.LIKE)
    private String roleName;

    @Schema(description = "角色描述")
    @Query(expression = QueryType.LIKE)
    private String roleDesc;

    @Schema(description = "排序字段", hidden = true)
    @QuerySort(value = "sortOrder")
    private String sortColumn;

    @Schema(description = "排序方式", hidden = true)
    @QueryOrder(value = "asc")
    private String sortOrder;
}
