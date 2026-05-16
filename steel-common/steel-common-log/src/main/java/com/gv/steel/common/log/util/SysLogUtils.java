package com.gv.steel.common.log.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.common.core.context.SpringContextHolder;
import com.gv.steel.common.core.context.UserInfoContextHolder;
import com.gv.steel.common.core.vo.UserInfo;
import com.gv.steel.common.log.event.SysLogEventSource;
import com.gv.steel.common.log.properties.LogProperties;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * 系统日志工具类
 */
@UtilityClass
public class SysLogUtils {

    public SysLogEventSource getSysLog() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysLogEventSource sysLog = new SysLogEventSource();
        sysLog.setType(LogTypeEnum.NORMAL.getType());
        String clientIP = ServletUtil.getClientIP(request);
        sysLog.setRemoteAddr(clientIP);
        sysLog.setRemoteAddrRegion(IPRegionUtil.getRegion(clientIP));
        sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        sysLog.setMethod(request.getMethod());
        sysLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        sysLog.setCreateBy(getUserId());
        sysLog.setCreateName(getUsername());
        sysLog.setUpdateBy(getUserId());
        sysLog.setUpdateName(getUsername());
        sysLog.setServiceId(getClientId());

        // get 参数脱敏
        LogProperties logProperties = SpringContextHolder.getBean(LogProperties.class);
        Map<String, String[]> parameterMap = MapUtil.removeAny(
                request.getParameterMap(),
                logProperties.getExcludeFields()
        );

        sysLog.setParams(HttpUtil.toParams(parameterMap));
        return sysLog;
    }

    /**
     * 获取客户端
     *
     * @return clientId
     */
    private String getClientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2AuthenticatedPrincipal) {
            OAuth2AuthenticatedPrincipal auth2Authentication = (OAuth2AuthenticatedPrincipal) principal;
            return MapUtil.getStr(auth2Authentication.getAttributes(), SecurityConstants.CLIENT_ID);
        }
        return null;
    }

    /**
     * 获取用户ID
     *
     * @return userId
     */
    private static Long getUserId() {
        UserInfo userInfo = UserInfoContextHolder.current();
        if (userInfo == null) {
            return null;
        }
        return userInfo.getUserId();
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    private String getUsername() {
        UserInfo userInfo = UserInfoContextHolder.current();
        if (userInfo == null) {
            return null;
        }
        return userInfo.getNickname();
    }

}
