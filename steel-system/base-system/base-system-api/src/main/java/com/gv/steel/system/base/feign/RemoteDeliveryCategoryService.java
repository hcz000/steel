package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.DeliveryCategory;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(contextId = "remoteDeliveryCategoryService", name = BaseAppConstant.APP_NAME)
public interface RemoteDeliveryCategoryService {
    @GetMapping("/delivery-category/{id:\\d+}")
    Result<DeliveryCategory> findDeliveryCategoryById(@PathVariable("id") Long id);

    @Operation(summary = "出货单类型列表")
    @GetMapping("/delivery-category/list")
    Result<List<DeliveryCategory>> findDeliveryCategoryList();
}
