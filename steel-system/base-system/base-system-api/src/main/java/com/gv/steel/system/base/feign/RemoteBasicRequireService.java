package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.CrosscutRequire;
import com.gv.steel.system.base.entity.RipCutRequire;
import com.gv.steel.system.base.entity.RollingRequire;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remoteBasicRequireService", value = BaseAppConstant.APP_NAME)
public interface RemoteBasicRequireService {

    @GetMapping(value = "/basic-require/factoryCrosscutRequire/{factoryId:\\d+}")
    Result<CrosscutRequire> getCrosscutRequireByFactoryId(@PathVariable("factoryId") Long factoryId);

    @GetMapping(value = "/basic-require/factoryRipCutRequire/{factoryId:\\d+}")
    Result<RipCutRequire> getRipCutRequireByFactoryId(@PathVariable("factoryId") Long factoryId);

    @GetMapping(value = "/basic-require/factoryRollingRequire/{factoryId:\\d+}")
    Result<RollingRequire> getRollingRequireByFactoryId(@PathVariable("factoryId") Long factoryId);

    @GetMapping(value = "/basic-require/customerRipCutRequire/{customerId:\\d+}")
    Result<RipCutRequire> getRipCutRequireByCustomerId(@PathVariable("customerId") Long customerId);

    @GetMapping(value = "/basic-require/customerCrosscutReq/{customerId:\\d+}")
    Result<CrosscutRequire> getCrosscutRequireByCustomerId(@PathVariable("customerId") Long customerId);

    @GetMapping(value = "/basic-require/customerRollingRequire/{customerId:\\d+}")
    Result<RollingRequire> getRollingRequireByCustomerId(@PathVariable("customerId") Long customerId);
}
