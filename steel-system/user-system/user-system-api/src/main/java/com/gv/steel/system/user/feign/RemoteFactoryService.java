package com.gv.steel.system.user.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.user.constant.UserAppConstant;
import com.gv.steel.system.user.entity.SysFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(contextId = "remoteFactoryService", value = UserAppConstant.APP_NAME)
public interface RemoteFactoryService {

    @GetMapping("/sys-factory/{id:\\d+}")
    public Result<SysFactory> findSysFactoryById(@PathVariable("id") Long id);

    @GetMapping("/sys-factory/list")
    public Result<List<SysFactory>> getList();
}
