package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.CustomerProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProduceCustomerService", value = BaseAppConstant.PRODUCE_SERVICE)
public interface RemoteProduceCustomerService {

    @PostMapping("/customer-profile")
    Result<Boolean> save(@RequestBody CustomerProfile customerProfile);

    @PutMapping("/customer-profile")
    Result<Boolean> update(@RequestBody CustomerProfile customerProfile);

    @DeleteMapping("/customer-profile/{id:\\d+}")
    Result<Boolean> delete(@PathVariable("id") Long id);
}
