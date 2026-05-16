package com.gv.steel.auth.support.handler;

import com.gv.steel.common.core.context.SpringContextHolder;
import com.gv.steel.common.core.util.WebUtils;
import com.gv.steel.common.log.event.SysLogEvent;
import com.gv.steel.common.log.event.SysLogEventSource;
import com.gv.steel.common.log.util.SysLogUtils;
import com.gv.steel.common.security.service.OAuthSysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 事件机制处理退出相关
 */
@Slf4j
@Component
public class CustomLogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            handle(authentication);
        }
    }

    /**
     * 处理退出成功方法
     * <p>
     * 获取到登录的authentication 对象
     *
     * @param authentication 登录对象
     */
    public void handle(Authentication authentication) {
        OAuthSysUser oAuthSysUser = (OAuthSysUser) authentication.getPrincipal();
        log.info("用户：{} 退出成功", oAuthSysUser);
        SysLogEventSource logVo = SysLogUtils.getSysLog();
        logVo.setTitle("退出成功");
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Long endTime = System.currentTimeMillis();
        logVo.setTime(endTime - startTime);

        // 设置对应的token
        WebUtils.getRequest().ifPresent(request -> logVo.setParams(request.getHeader(HttpHeaders.AUTHORIZATION)));

        // 这边设置ServiceId
        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            logVo.setServiceId(authentication.getCredentials().toString());
        }
        logVo.setCreateBy(oAuthSysUser.getId());
        logVo.setCreateName(oAuthSysUser.getNickname());
        logVo.setUpdateBy(oAuthSysUser.getId());
        logVo.setUpdateName(oAuthSysUser.getNickname());
        SpringContextHolder.publishEvent(new SysLogEvent(logVo));
    }

}
