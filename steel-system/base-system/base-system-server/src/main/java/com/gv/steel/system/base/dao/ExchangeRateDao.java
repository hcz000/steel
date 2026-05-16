package com.gv.steel.system.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.dto.ExchangeRateQueryDTO;
import com.gv.steel.system.base.entity.ExchangeRate;
import com.gv.steel.system.base.vo.ExchangeRatePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 汇率管理表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Mapper
public interface ExchangeRateDao extends BaseDao<ExchangeRate> {

    long selectExchangeRatePage_CONUNT(@Param("param") ExchangeRateQueryDTO dto);

    Page<ExchangeRatePageVO> selectExchangeRatePage(Page<ExchangeRatePageVO> page, @Param("param") ExchangeRateQueryDTO dto);
}
