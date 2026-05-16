package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 角色表
 * </p>
 */
@Data
@TableName("sys_role")
@Schema(description = "角色")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity<SysRole> {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "角色名称 不能为空")
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank(message = "角色标识 不能为空")
    @Schema(description = "角色标识")
    private String roleCode;

    @NotBlank(message = "角色描述 不能为空")
    @Schema(description = "角色描述")
    private String roleDesc;

    @Schema(description = "角色排序值")
    private Integer sortOrder;
}
