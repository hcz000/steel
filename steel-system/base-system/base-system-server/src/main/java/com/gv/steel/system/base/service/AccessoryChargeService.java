package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.AccessoryChargeQueryDTO;
import com.gv.steel.system.base.entity.AccessoryCharge;
import com.gv.steel.system.base.vo.AccessoryChargePageVO;
import com.gv.steel.system.base.vo.ProcessAccessoryVO;

import java.util.List;

/**
 * <p>
 * 辅料收费 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface AccessoryChargeService extends BaseService<AccessoryCharge> {

    PageResult<AccessoryChargePageVO> pageAccessoryCharge(Page<AccessoryChargePageVO> page, AccessoryChargeQueryDTO dto);

    List<ProcessAccessoryVO> getAccessoryChargeByScope(Long customerId, Integer useScope);

    boolean saveOrUpdateBatch(Long contractId, List<AccessoryCharge> accessoryChargeList);

    List<AccessoryCharge> getAccessoryChargeListByCustomer(Long customerId);
}
