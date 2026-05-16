package com.gv.steel.system.user.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.system.user.constant.UserAppConstant;
import com.gv.steel.system.user.entity.SysOauthClientDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(contextId = "remoteClientDetailsService", value = UserAppConstant.APP_NAME)
public interface RemoteClientDetailsService {

    /**
     * 通过clientId 查询客户端信息
     *
     * @param clientId 用户名
     * @return R
     */
    @GetMapping(value = "/client/getClientDetailsById/{clientId}", headers = SecurityConstants.HEADER_FROM_IN)
    Result<SysOauthClientDetails> getClientDetailsById(@PathVariable("clientId") String clientId);

    /**
     * 查询全部客户端
     *
     * @return R
     */
    @GetMapping(value = "/client/list", headers = SecurityConstants.HEADER_FROM_IN)
    Result<List<SysOauthClientDetails>> listClientDetails();

}
