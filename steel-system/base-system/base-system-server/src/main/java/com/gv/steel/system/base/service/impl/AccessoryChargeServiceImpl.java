package com.gv.steel.system.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.AccessoryChargeDao;
import com.gv.steel.system.base.dao.ProcessingContractDao;
import com.gv.steel.system.base.dto.AccessoryChargeQueryDTO;
import com.gv.steel.system.base.entity.AccessoryCharge;
import com.gv.steel.system.base.entity.ProcessingContract;
import com.gv.steel.system.base.service.AccessoryChargeService;
import com.gv.steel.system.base.vo.AccessoryChargePageVO;
import com.gv.steel.system.base.vo.ProcessAccessoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 辅料收费 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccessoryChargeServiceImpl extends BaseServiceImpl<AccessoryChargeDao, AccessoryCharge> implements AccessoryChargeService {
    private final ProcessingContractDao processingContractDao;

    @Override
    public PageResult<AccessoryChargePageVO> pageAccessoryCharge(Page<AccessoryChargePageVO> page, AccessoryChargeQueryDTO dto) {
        baseDao.selectAccessoryChargePage(page, dto);
        return PageResult.<AccessoryChargePageVO>builder().build().pageResult(page);
    }

    @Override
    public List<ProcessAccessoryVO> getAccessoryChargeByScope(Long customerId, Integer useScope) {
        return baseDao.selectByCustomerIdAndUseScope(customerId, useScope);
    }

    @Override
    public boolean saveOrUpdateBatch(Long contractId, List<AccessoryCharge> accessoryChargeList) {
        accessoryChargeList.forEach(item -> item.setContractId(contractId));
        return subtractSaveOrUpdateBatch(contractId, accessoryChargeList, AccessoryCharge::getContractId, AccessoryCharge::getId);
    }

    @Override
    public List<AccessoryCharge> getAccessoryChargeListByCustomer(Long customerId) {
        ProcessingContract processingContract = processingContractDao.selectOne(Wrappers.<ProcessingContract>lambdaQuery().eq(ProcessingContract::getCustomerId, customerId));
        List<AccessoryCharge> accessoryChargeList = Lists.newArrayList();
        Optional.ofNullable(processingContract).ifPresent(contract -> {
            accessoryChargeList.addAll(
                    list(Wrappers.<AccessoryCharge>lambdaQuery().eq(AccessoryCharge::getContractId, contract.getId()))
            );
        });
        return accessoryChargeList;
    }
}
