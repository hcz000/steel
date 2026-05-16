package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.ExchangeRateQueryDTO;
import com.gv.steel.system.base.entity.ExchangeRate;
import com.gv.steel.system.base.vo.ExchangeRatePageVO;

/**
 * <p>
 * 汇率管理表 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface ExchangeRateService extends BaseService<ExchangeRate> {

    /**
     * 汇率管理表查询列表
     *
     * @param page 分页参数
     * @param dto  查询参数
     * @return Page<ExchangeRate>
     */
    PageResult<ExchangeRatePageVO> getPage(Page<ExchangeRatePageVO> page, ExchangeRateQueryDTO dto);

    /**
     * 修改汇率信息
     *
     * @param exchangeRate 汇率
     * @return result
     */
    boolean updateExchangeRateById(ExchangeRate exchangeRate);

    boolean saveExchangeRate(ExchangeRate exchangeRate);
}
