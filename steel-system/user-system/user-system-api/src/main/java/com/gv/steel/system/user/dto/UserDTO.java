package com.gv.steel.system.user.dto;

import com.gv.steel.system.user.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Schema(description = "用户信息操作对象")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {

    private static final long serialVersionUID = -220465512541646436L;
    /**
     * 角色ID
     */
    @Schema(description = "角色id集合")
    private List<Long> role;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "所属工厂ID")
    private List<Long> factoryIds;

    @Schema(description = "所属班组ID")
    private List<Long> teamIds;

    /**
     * 新密码
     */
    @Schema(description = "修改密码新密码")
    private String newPassword;

    /**
     * 验证码
     */
    @Schema(description = "短信验证码")
    private String code;

}
