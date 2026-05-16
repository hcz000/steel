package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "RemoteProduceRawMaterialService", name = "produce-system-server")
public interface RemoteProduceRawMaterialService {

    @GetMapping("/raw-material/existsInventory/{customerId:\\d+}")
    Result<Boolean> existsInventory(@PathVariable("customerId") Long customerId);
}
