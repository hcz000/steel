package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.RipCutChargeQueryDTO;
import com.gv.steel.system.base.entity.RipCutCharge;
import com.gv.steel.system.base.vo.RipCutChargePageVO;

import java.util.List;

/**
 * <p>
 * 纵切收费 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface RipCutChargeService extends BaseService<RipCutCharge> {

    /**
     * 通过委托单位ID获取纵切收费列表
     *
     * @param customerId 委托单位ID
     * @return 收费列表
     */
    List<RipCutCharge> findListByCustomerId(Long customerId);

    boolean saveOrUpdateBatch(Long contractId, List<RipCutCharge> ripCutChargeList);

    PageResult<RipCutChargePageVO> pageRipCutCharge(Page<RipCutChargePageVO> page, RipCutChargeQueryDTO dto);
}
