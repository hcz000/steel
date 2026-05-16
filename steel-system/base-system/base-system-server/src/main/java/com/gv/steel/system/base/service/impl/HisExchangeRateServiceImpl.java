package com.gv.steel.system.base.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.HisExchangeRateDao;
import com.gv.steel.system.base.entity.HisExchangeRate;
import com.gv.steel.system.base.service.HisExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 历史汇率表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2024-02-05
 */
@Slf4j
@Service
@Transactional
public class HisExchangeRateServiceImpl extends BaseServiceImpl<HisExchangeRateDao, HisExchangeRate> implements HisExchangeRateService {

}
