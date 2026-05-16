package com.gv.steel.system.base.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.entity.HisExchangeRate;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 历史汇率表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2024-02-05
 */
@Mapper
public interface HisExchangeRateDao extends BaseDao<HisExchangeRate> {

}
