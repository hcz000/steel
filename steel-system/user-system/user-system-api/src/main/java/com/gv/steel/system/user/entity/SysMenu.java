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
 * 菜单权限表
 * </p>
 */
@Data
@TableName("sys_menu")
@Schema(description = "菜单")
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity<SysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 菜单权限标识
     */
    @Schema(description = "菜单权限标识")
    private String permission;

    /**
     * 父菜单ID
     */
    @NotNull(message = "菜单父ID不能为空")
    @Schema(description = "菜单父id")
    private Long parentId;

    /**
     * 图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 前端URL
     */
    @Schema(description = "前端路由标识路径")
    private String path;

    /**
     * 前端组件路径
     */
    @Schema(description = "前端组件路径")
    private String component;

    /**
     * 菜单CODE值
     */
    @Schema(description = "菜单CODE值")
    private String code;

    /**
     * 排序值
     */
    @Schema(description = "排序值")
    private Integer sortOrder;

    /**
     * 菜单类型 （0目录 1菜单 2按钮）
     */
    @NotNull(message = "菜单类型不能为空")
    @Schema(description = "菜单类型 （0目录 1菜单 2按钮）")
    private Integer type;

    /**
     * 路由缓冲
     */
    @Schema(description = "路由缓冲")
    private Integer keepAlive;

    /**
     * 是否隐藏
     */
    @Schema(description = "是否隐藏")
    private Integer hidden;

    /**
     * 是否启动
     */
    @Schema(description = "是否启动")
    private Integer enable;
}
