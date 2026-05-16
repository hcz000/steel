package com.gv.steel.system.user.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.user.entity.SysUserTeam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户班组关联关系表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface SysUserTeamDao extends BaseDao<SysUserTeam> {
    /**
     * 通过userId获取班组ID
     *
     * @param userId 用户ID
     * @return 班组ID集合
     */
    List<Long> listTeamIdsByUserId(String userId);
}
