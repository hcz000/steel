package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.vo.ProcessingContractDetailVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remoteProcessingContractService", value = BaseAppConstant.APP_NAME)
public interface RemoteProcessingContractService {

    @GetMapping(value = "/processing-contract/customer-contract/{customerId:\\d+}", headers = SecurityConstants.HEADER_FROM_IN)
    Result<ProcessingContractDetailVO> findProcessingContractByCustomerId(@PathVariable("customerId") Long customerId);
}
