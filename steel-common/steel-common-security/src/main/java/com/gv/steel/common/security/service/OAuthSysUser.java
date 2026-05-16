package com.gv.steel.common.security.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 扩展用户信息
 */
@Getter
public class OAuthSysUser extends User implements OAuth2AuthenticatedPrincipal {
    private static final long serialVersionUID = -2315887922341861748L;

    private final Map<String, Object> attributes = new HashMap<>();
    /**
     * 用户ID
     */
    private final Long id;
    /**
     * 昵称
     */
    private final String nickname;
    /**
     * 部门ID
     */
    private final Long deptId;
    /**
     * 手机号
     */
    @Getter
    private final String phone;

    public OAuthSysUser(
            Long id,
            Long deptId,
            String phone,
            String username,
            String nickname,
            String password,
            boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.deptId = deptId;
        this.phone = phone;
        this.nickname = nickname;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }
}
