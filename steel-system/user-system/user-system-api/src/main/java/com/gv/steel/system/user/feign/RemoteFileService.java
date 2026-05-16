package com.gv.steel.system.user.feign;

import com.gv.steel.system.user.constant.UserAppConstant;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remoteFileService", value = UserAppConstant.APP_NAME)
public interface RemoteFileService {

    @GetMapping("/sys-file/{id:\\d+}")
    Response download(@PathVariable("id") Long id);
}
