package com.gv.steel.system.user.vo;

import com.gv.steel.system.user.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SZB
 */
@Data
@Schema(description = "用户详情信息对象")
@EqualsAndHashCode(callSuper = true)
public class UserDetailVO extends SysUser {

    /**
     * 部门名称
     */
    @Schema(title = "部门名称")
    private String deptName;

    private static final long serialVersionUID = -2046278943827103117L;
    @Schema(description = "角色id集合")
    private List<Long> role;

    @Schema(description = "所属工厂ID")
    private List<Long> factoryIds;

    @Schema(description = "所属班组ID")
    private List<Long> teamIds;
}
