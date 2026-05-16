package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.CurrencyPageDTO;
import com.gv.steel.system.base.entity.Currency;

/**
 * <p>
 * 币种管理表 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface CurrencyService extends BaseService<Currency> {

    /**
     * 币种管理表查询列表
     *
     * @param page 分页参数
     * @param dto  查询参数
     * @return Page<Currency>
     */
    Page<Currency> getPage(Page<Currency> page, CurrencyPageDTO dto);

    @Override
    boolean save(Currency currency);
}
