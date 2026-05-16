package com.gv.steel.system.user.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.user.entity.SysFactory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 工厂管理 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface SysFactoryDao extends BaseDao<SysFactory> {

    /**
     * 通过userId查询所属工厂
     *
     * @param userId 用户ID
     * @return 工厂集合
     */
    List<SysFactory> listFactoriesByUserId(Long userId);
}
