package com.gv.steel.common.security.service;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.system.user.feign.RemoteUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * 用户详细信息
 */
@Slf4j
@Primary
@RequiredArgsConstructor
public class OAuthSysUserDetailsServiceImpl implements OAuthSysUserDetailsService {

    private final RemoteUserService remoteUserService;

    private final CacheManager cacheManager;

    /**
     * 用户名密码登录
     *
     * @param username 用户名
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null && cache.get(username) != null) {
            return (OAuthSysUser) Objects.requireNonNull(cache.get(username)).get();
        }

        Result<UserInfo> result = remoteUserService.info(username);
        return setUserCache(username, result, cacheManager);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}
