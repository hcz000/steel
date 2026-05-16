package com.gv.steel.system.user.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.user.entity.SysUserFactory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户工厂关联关系表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface SysUserFactoryDao extends BaseDao<SysUserFactory> {

    /**
     * 通过userId查询所属工厂ID
     *
     * @param userId 用户ID
     * @return 工厂ID集合
     */
    List<Long> listFactoryIdsByUserId(String userId);
}
