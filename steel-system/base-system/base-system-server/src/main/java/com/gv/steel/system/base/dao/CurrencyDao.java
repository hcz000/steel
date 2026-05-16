package com.gv.steel.system.base.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.entity.Currency;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 币种管理表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface CurrencyDao extends BaseDao<Currency> {

}
