package com.gv.steel.system.user.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.system.user.constant.UserAppConstant;
import com.gv.steel.system.user.entity.SysLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remoteLogService", value = UserAppConstant.APP_NAME)
public interface RemoteLogService {

    /**
     * 保存日志
     *
     * @param sysLog 日志实体
     * @return succes、false
     */
    @PostMapping(value = "/log", headers = SecurityConstants.HEADER_FROM_IN)
    Result<Boolean> saveLog(@RequestBody SysLog sysLog);

}
