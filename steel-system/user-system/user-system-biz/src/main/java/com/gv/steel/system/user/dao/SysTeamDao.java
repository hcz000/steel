package com.gv.steel.system.user.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.user.entity.SysTeam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 班组管理 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface SysTeamDao extends BaseDao<SysTeam> {

    /**
     * 通过userId获取班组
     *
     * @param userId 用户ID
     * @return 班组集合
     */
    List<SysTeam> listTeamsByUserId(Long userId);
}
