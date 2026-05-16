package com.gv.steel.common.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gv.steel.common.core.entity.SysFactoryInfo;
import com.gv.steel.common.core.entity.SysRoleInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 5150404146689674896L;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 随机盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 锁定标记
     */
    private Integer lockFlag;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 权限标识集合
     */
    private String[] permissions;

    /**
     * 角色集合
     */
    private Long[] roles;

    /**
     * 角色集合
     */
    private List<SysRoleInfo> roleList;

    /**
     * 工厂集合
     */
    private List<SysFactoryInfo> factoryList;
}
