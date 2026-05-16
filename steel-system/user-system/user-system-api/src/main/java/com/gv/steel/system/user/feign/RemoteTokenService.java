package com.gv.steel.system.user.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.system.user.constant.UserAppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(contextId = "remoteTokenService", value = UserAppConstant.AUTH_APP_NAME)
public interface RemoteTokenService {

    /**
     * 分页查询token 信息
     *
     * @param params 分页参数
     * @return page
     */
    @PostMapping(value = "/token/page", headers = SecurityConstants.HEADER_FROM_IN)
    Result getTokenPage(@RequestBody Map<String, Object> params);

    /**
     * 删除token
     *
     * @param token token
     * @return
     */
    @DeleteMapping(value = "/token/{token}", headers = SecurityConstants.HEADER_FROM_IN)
    Result<Boolean> removeToken(@PathVariable("token") String token);

}
