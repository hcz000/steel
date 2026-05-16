package com.gv.steel.system.user.dto;

import com.gv.steel.system.user.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema(description = "角色新增对象")
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends SysRole {

    private static final long serialVersionUID = -4995576247562092827L;
    /**
     * 角色部门Id
     */
    @Schema(description = "角色部门Id")
    private Long roleDeptId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

}
