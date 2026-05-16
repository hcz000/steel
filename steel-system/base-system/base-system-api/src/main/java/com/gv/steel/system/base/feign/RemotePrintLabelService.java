package com.gv.steel.system.base.feign;

import com.gv.steel.system.base.constant.BaseAppConstant;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remotePrintLabelService", name = BaseAppConstant.APP_NAME)
public interface RemotePrintLabelService {

    @GetMapping("/print-label/template/code/{code}")
    Response getTemplateByCode(@PathVariable("code") String code);
}
