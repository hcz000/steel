package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.RollingChargeQueryDTO;
import com.gv.steel.system.base.entity.RollingCharge;
import com.gv.steel.system.base.vo.RollingChargePageVO;

import java.util.List;

/**
 * <p>
 * 压延收费 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface RollingChargeService extends BaseService<RollingCharge> {

    PageResult<RollingChargePageVO> pageRollingCharge(Page<RollingChargePageVO> page, RollingChargeQueryDTO dto);

    List<RollingCharge> findListByCustomerId(Long customerId);

    boolean saveOrUpdateBatch(Long contractId, List<RollingCharge> rollingChargeList);
}
