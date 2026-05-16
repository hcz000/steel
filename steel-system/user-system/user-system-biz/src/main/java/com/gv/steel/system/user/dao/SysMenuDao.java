package com.gv.steel.system.user.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.user.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 */
@Mapper
public interface SysMenuDao extends BaseDao<SysMenu> {

    /**
     * 通过角色编号查询菜单
     *
     * @param roleId 角色ID
     * @return
     */
    Set<SysMenu> listMenusByRoleId(Long roleId);

}
