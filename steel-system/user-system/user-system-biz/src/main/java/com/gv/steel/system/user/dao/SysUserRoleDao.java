package com.gv.steel.system.user.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.user.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 */
@Mapper
public interface SysUserRoleDao extends BaseDao<SysUserRole> {

    /**
     * 根据用户Id删除该用户的角色关系
     *
     * @param userId 用户ID
     * @return boolean
     */
    Boolean deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询角色ID
     *
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<Long> listRoleIdsByUserId(@Param("userId") Long userId);

}
