package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.InvoiceTitle;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(contextId = "remoteInvoiceTitleService", value = BaseAppConstant.APP_NAME)
public interface RemoteInvoiceTitleService {

    @GetMapping("/invoice-title/list")
    Result<List<InvoiceTitle>> findDeliveryCategoryList();
}
