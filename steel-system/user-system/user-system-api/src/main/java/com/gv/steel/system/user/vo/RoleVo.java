package com.gv.steel.system.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "前端角色展示对象")
public class RoleVo {

    /**
     * 角色id
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 菜单列表
     */
    @Schema(description = "菜单列表")
    private List<Long> menuIds;

}
