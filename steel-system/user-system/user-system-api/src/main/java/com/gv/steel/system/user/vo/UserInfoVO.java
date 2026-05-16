package com.gv.steel.system.user.vo;

import com.gv.steel.common.core.entity.SysFactoryInfo;
import com.gv.steel.system.user.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * commit('SET_ROLES', data) commit('SET_NAME', data) commit('SET_AVATAR', data)
 * commit('SET_INTRODUCTION', data) commit('SET_PERMISSIONS', data)
 */
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 810350526599853968L;
    /**
     * 用户基本信息
     */
    private SysUser sysUser;

    /**
     * 权限标识集合
     */
    private String[] permissions;

    /**
     * 角色集合
     */
    private Long[] roles;

    /**
     * 工厂集合
     */
    private List<SysFactoryInfo> factoryList;

}
