package com.gv.steel.common.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 */
@Data
public class SysRoleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String roleName;

    private String roleCode;

    private String roleDesc;
}
