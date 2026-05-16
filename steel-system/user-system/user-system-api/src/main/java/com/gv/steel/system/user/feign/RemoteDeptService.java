package com.gv.steel.system.user.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.system.user.constant.UserAppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(contextId = "remoteDeptService", value = UserAppConstant.APP_NAME)
public interface RemoteDeptService {

    /**
     * 查收子级id列表
     *
     * @return 返回子级id列表
     */
    @GetMapping(value = "/dept/child-id/{deptId}", headers = SecurityConstants.HEADER_FROM_IN)
    Result<List<Long>> listChildDeptId(@PathVariable("deptId") Long deptId);

}
