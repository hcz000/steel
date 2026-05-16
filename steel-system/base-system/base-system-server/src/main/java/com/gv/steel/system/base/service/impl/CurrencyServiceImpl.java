package com.gv.steel.system.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.mybatis.base.entity.CommonModel;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.CurrencyDao;
import com.gv.steel.system.base.dto.CurrencyPageDTO;
import com.gv.steel.system.base.entity.Currency;
import com.gv.steel.system.base.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 币种管理表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class CurrencyServiceImpl extends BaseServiceImpl<CurrencyDao, Currency> implements CurrencyService {

    @Override
    public Page<Currency> getPage(Page<Currency> page, CurrencyPageDTO dto) {
        LambdaQueryWrapper<Currency> wrapper = getWrapper(dto);
        return this.page(page, wrapper);
    }

    @Override
    public boolean save(Currency currency) {
        String shortName = currency.getShortName();
        List<Currency> list = this.list(Wrappers.<Currency>lambdaQuery().eq(Currency::getShortName, shortName));
        if (!CollUtil.isEmpty(list)) throw new BaseException(MsgUtils.getMessage("currency.short.name.is.exist"));
        return baseDao.insert(currency) > 0;
    }

    private LambdaQueryWrapper<Currency> getWrapper(CurrencyPageDTO dto) {
        LambdaQueryWrapper<Currency> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CommonModel::getCreateTime);
        if (ObjectUtil.isNotEmpty(dto)) {
            if (StrUtil.isNotEmpty(dto.getName())) {
                wrapper.like(Currency::getName, dto.getName());
            }
            if (ArrayUtil.isNotEmpty(dto.getTimeZone())) {
                wrapper.between(CommonModel::getCreateTime, dto.getTimeZone()[0], dto.getTimeZone()[1]);
            }
        }
        return wrapper;
    }
}
