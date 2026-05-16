package com.gv.steel.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.common.core.util.ResultOps;
import com.gv.steel.common.core.vo.UserInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface OAuthSysUserDetailsService extends UserDetailsService, Ordered {

    /**
     * 是否支持此客户端校验
     *
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 构建user_details
     *
     * @param result 用户信息
     * @return UserDetails
     */
    default UserDetails getUserDetails(Result<UserInfo> result) {
        UserInfo info = ResultOps.of(result).getData().orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        Set<String> dbAuthsSet = new HashSet<>();

        if (ArrayUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

        }

        Collection<GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));

        // 构造security用户
        return new OAuthSysUser(info.getUserId(), info.getDeptId(), info.getPhone(), info.getUsername(), info.getNickname(),
                SecurityConstants.BCRYPT + info.getPassword(), true, true, true,
                CommonConstants.STATUS_NORMAL == info.getLockFlag(), authorities);
    }

    /**
     * 通过用户实体查询
     *
     * @param authSysUser user
     * @return
     */
    default UserDetails loadUserByUser(OAuthSysUser authSysUser) {
        return this.loadUserByUsername(authSysUser.getUsername());
    }

    default UserDetails setUserCache(String id, Result<UserInfo> result, CacheManager cacheManager) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        UserDetails userDetails = getUserDetails(result);
        if (cache != null) {
            cache.put(id, userDetails);
        }
        Cache userInfoCache = cacheManager.getCache(CacheConstants.USER_INFO_DETAILS);
        UserInfo userInfo = result.getData();
        if (userInfoCache != null) {
            userInfoCache.put(userInfo.getUserId(), userInfo);
        }
        return userDetails;
    }

}
