package com.gv.steel.common.security.filter;

import cn.hutool.core.util.ObjectUtil;
import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.core.context.UserInfoContextHolder;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.common.security.service.OAuthSysUser;
import com.gv.steel.common.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@ConditionalOnClass({Filter.class})
public class UserInfoFilter extends OncePerRequestFilter {
    @Autowired
    private CacheManager cacheManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Cache cache = cacheManager.getCache(CacheConstants.USER_INFO_DETAILS);
            Optional.ofNullable(cache).ifPresent(cache1 -> {
                OAuthSysUser user = SecurityUtils.getUser();
                if (ObjectUtil.isNotNull(user)) {
                    Long id = user.getId();
                    UserInfoContextHolder.put((UserInfo) cache1.get(id).get());
                }
            });
            filterChain.doFilter(request, response);
        } finally {
            UserInfoContextHolder.clear();
        }
    }
}
