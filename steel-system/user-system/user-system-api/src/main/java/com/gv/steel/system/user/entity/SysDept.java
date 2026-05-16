package com.gv.steel.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 部门管理
 * </p>
 */
@Data
@TableName("sys_dept")
@Schema(description = "部门")
@EqualsAndHashCode(callSuper = true)
public class SysDept extends BaseEntity<SysDept> {

    private static final long serialVersionUID = 1L;
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称", required = true)
    private String name;

    /**
     * 排序
     */
    @NotNull(message = "部门排序值不能为空")
    @Schema(description = "排序值", required = true)
    private Integer sortOrder;

    /**
     * 父级部门id
     */
    @Schema(description = "父级部门id")
    private Long parentId;
}
