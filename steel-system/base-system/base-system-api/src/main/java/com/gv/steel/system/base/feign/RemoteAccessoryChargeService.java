package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.AccessoryCharge;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteAccessoryChargeService", value = BaseAppConstant.APP_NAME)
public interface RemoteAccessoryChargeService {

    @GetMapping("/accessory-charge/list")
    Result<List<AccessoryCharge>> getAccessoryChargeList(@RequestParam("customerId") Long customerId);
}
