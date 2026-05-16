package com.gv.steel.system.user.vo;

import com.gv.steel.system.user.entity.SysFactory;
import com.gv.steel.system.user.entity.SysRole;
import com.gv.steel.system.user.entity.SysTeam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "UserVO", description = "用户VO")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(title = "主键ID")
    private Long id;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    private String username;

    /**
     * 用户昵称
     */
    @Schema(title = "用户昵称")
    private String nickname;

    /**
     * 密码
     */
    @Schema(title = "密码")
    private String password;

    /**
     * 随机盐
     */
    @Schema(title = "随机盐")
    private String salt;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(title = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 0-正常，1-删除
     */
    @Schema(title = "删除标志（0-正常，1-删除")
    private Integer delFlag;

    /**
     * 锁定标记
     */
    @Schema(title = "锁定标记")
    private Integer lockFlag;

    /**
     * 电话
     */
    @Schema(title = "电话")
    private String phone;

    /**
     * 头像
     */
    @Schema(title = "头像")
    private String avatar;

    /**
     * 部门ID
     */
    @Schema(title = "部门ID")
    private Long deptId;

    /**
     * 部门名称
     */
    @Schema(title = "部门名称")
    private String deptName;

    /**
     * 角色列表
     */
    @Schema(title = "角色列表")
    private List<SysRole> roleList;

    /**
     * 工厂列表
     */
    @Schema(title = "工厂列表")
    private List<SysFactory> factoryList;

    /**
     * 班组列表
     */
    @Schema(title = "班组列表")
    private List<SysTeam> teamList;
}
