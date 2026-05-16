package com.gv.steel.common.security.service;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.system.user.feign.RemoteUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * 用户详细信息
 */
@Slf4j
@RequiredArgsConstructor
public class AppOAuthSysUserDetailsServiceImpl implements OAuthSysUserDetailsService {

    private final RemoteUserService remoteUserService;

    private final CacheManager cacheManager;

    /**
     * 手机号登录
     *
     * @param phone 手机号
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String phone) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null && cache.get(phone) != null) {
            return (OAuthSysUser) Objects.requireNonNull(cache.get(phone)).get();
        }

        Result<UserInfo> result = remoteUserService.infoByMobile(phone);

        return setUserCache(phone, result, cacheManager);
    }

    /**
     * check-token 使用
     *
     * @param authSysUser user
     * @return
     */
    @Override
    public UserDetails loadUserByUser(OAuthSysUser authSysUser) {
        return this.loadUserByUsername(authSysUser.getPhone());
    }

    /**
     * 是否支持此客户端校验
     *
     * @param clientId 目标客户端
     * @return true/false
     */
    @Override
    public boolean support(String clientId, String grantType) {
        return SecurityConstants.APP.equals(grantType);
    }

}
