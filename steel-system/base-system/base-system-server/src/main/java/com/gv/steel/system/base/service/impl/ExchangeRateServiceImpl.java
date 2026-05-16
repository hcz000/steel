package com.gv.steel.system.base.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.mybatis.base.entity.CommonModel;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.ExchangeRateDao;
import com.gv.steel.system.base.dto.ExchangeRateQueryDTO;
import com.gv.steel.system.base.entity.ExchangeRate;
import com.gv.steel.system.base.entity.HisExchangeRate;
import com.gv.steel.system.base.service.ExchangeRateService;
import com.gv.steel.system.base.service.HisExchangeRateService;
import com.gv.steel.system.base.vo.ExchangeRatePageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * <p>
 * 汇率管理表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeRateServiceImpl extends BaseServiceImpl<ExchangeRateDao, ExchangeRate> implements ExchangeRateService {

    private final HisExchangeRateService hisExchangeRateService;

    @Override
    public PageResult<ExchangeRatePageVO> getPage(Page<ExchangeRatePageVO> page, ExchangeRateQueryDTO dto) {
        page.setCountId("selectExchangeRatePage_CONUNT");
        baseDao.selectExchangeRatePage(page, dto);
        return PageResult.<ExchangeRatePageVO>builder().build().pageResult(page);
    }

    @Override
    public boolean updateExchangeRateById(ExchangeRate exchangeRate) {
        ExchangeRate dbRate = getById(exchangeRate.getId());
        // 如果汇率有变化，记录历史记录
        if (!dbRate.getExchangeRate().equals(exchangeRate.getExchangeRate())) {
            HisExchangeRate his = new HisExchangeRate();
            his.setSourceName(dbRate.getSourceName());
            his.setSourceShortName(dbRate.getSourceShortName());
            his.setTargetName(dbRate.getTargetName());
            his.setTargetShortName(dbRate.getTargetShortName());
            his.setExchangeRate(dbRate.getExchangeRate());
            his.setNid(dbRate.getId());
            hisExchangeRateService.save(his);
        }
        return updateById(exchangeRate);
    }

    @Override
    public boolean saveExchangeRate(ExchangeRate exchangeRate) {
        if (exists(
                Wrappers.<ExchangeRate>lambdaQuery()
                        .eq(ExchangeRate::getSourceShortName, exchangeRate.getSourceShortName())
                        .eq(ExchangeRate::getTargetShortName, exchangeRate.getTargetShortName())
        )) {
            throw new BaseException(MsgUtils.getMessage("exchange.rate.exists"));
        }

        return save(exchangeRate);
    }

    private LambdaQueryWrapper<ExchangeRate> getWrapper(ExchangeRateQueryDTO dto) {
        LambdaQueryWrapper<ExchangeRate> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CommonModel::getCreateTime);
        if (ObjectUtil.isNotEmpty(dto)) {
            if (StrUtil.isNotEmpty(dto.getSourceShortName()) && StrUtil.isNotEmpty(dto.getTargetShortName())) {
                wrapper.eq(ExchangeRate::getSourceShortName, dto.getSourceShortName())
                        .eq(ExchangeRate::getTargetShortName, dto.getTargetShortName());
            }
            if (ArrayUtil.isNotEmpty(dto.getTimeZone())) {
                LocalDate[] timeZone = dto.getTimeZone();
                wrapper.between(CommonModel::getCreateTime, timeZone[0], timeZone[1]);
            }
        }
        return wrapper;
    }
}
