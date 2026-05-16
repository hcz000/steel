package com.gv.steel.system.user.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.system.user.constant.UserAppConstant;
import com.gv.steel.system.user.entity.SysUser;
import com.gv.steel.system.user.vo.UserDetailVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(contextId = "remoteUserService", value = UserAppConstant.APP_NAME)
public interface RemoteUserService {

    /**
     * 通过用户名查询用户、角色信息
     *
     * @param username 用户名
     * @return R
     */
    @GetMapping(value = "/user/info/{username}", headers = SecurityConstants.HEADER_FROM_IN)
    Result<UserInfo> info(@PathVariable("username") String username);

    /**
     * 通过手机号码查询用户、角色信息
     *
     * @param phone 手机号码
     * @return R
     */
    @GetMapping(value = "/app/info/{phone}", headers = SecurityConstants.HEADER_FROM_IN)
    Result<UserInfo> infoByMobile(@PathVariable("phone") String phone);

    /**
     * 根据部门id，查询对应的用户 id 集合
     *
     * @param deptIds 部门id 集合
     * @return 用户 id 集合
     */
    @GetMapping(value = "/user/ids", headers = SecurityConstants.HEADER_FROM_IN)
    Result<List<Long>> listUserIdByDeptIds(@RequestParam("deptIds") Set<Long> deptIds);

    @GetMapping(value = "/user/{id:\\d+}")
    Result<UserDetailVO> getUserById(@PathVariable("id") Long id);

    /**
     * 通过用户ID获取用户的基本信息，包括用户名、昵称、部门ID、电话
     *
     * @param id 用户ID
     * @return 用户基本信息
     */
    @GetMapping(value = "/user/base/{id:\\d+}", headers = SecurityConstants.HEADER_FROM_IN)
    Result<SysUser> getBaseInfo(@PathVariable("id") Long id);
}
