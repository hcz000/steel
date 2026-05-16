package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.CrossCutChargeQueryDTO;
import com.gv.steel.system.base.entity.CrossCutCharge;
import com.gv.steel.system.base.vo.CrossCutChargePageVO;

import java.util.List;

/**
 * <p>
 * 横切收费 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface CrossCutChargeService extends BaseService<CrossCutCharge> {

    PageResult<CrossCutChargePageVO> pageCrossCut(Page<CrossCutChargePageVO> page, CrossCutChargeQueryDTO dto);

    List<CrossCutCharge> findListByCustomerId(Long customerId);

    boolean saveOrUpdateBatch(Long contractId, List<CrossCutCharge> crossCutChargeList);
}
