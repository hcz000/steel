package com.gv.steel.system.base.feign;


import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.CrossCutCharge;
import com.gv.steel.system.base.entity.RipCutCharge;
import com.gv.steel.system.base.entity.RollingCharge;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteBasicChargeService", value = BaseAppConstant.APP_NAME)
public interface RemoteBasicChargeService {

    @GetMapping(value = "/rip-cut-charge/list", headers = SecurityConstants.HEADER_FROM_IN)
    Result<List<RipCutCharge>> ripCutChargeList(@RequestParam("customerId") Long customerId);

    @GetMapping(value = "/cross-cut-charge/list", headers = SecurityConstants.HEADER_FROM_IN)
    Result<List<CrossCutCharge>> crossCutChargeList(@RequestParam("customerId") Long customerId);

    @GetMapping(value = "/rolling-charge/list", headers = SecurityConstants.HEADER_FROM_IN)
    Result<List<RollingCharge>> rollingChargeList(@RequestParam("customerId") Long customerId);
}
