package com.gv.steel.common.feign.config;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

public class FeignCommonHeaderInterceptorConfig {
    private final List<String> commonHeaderList = new ArrayList<>();

    @PostConstruct
    public void initialize() {
        commonHeaderList.add("version");
        commonHeaderList.add("lang");
    }

    @Bean
    @ConditionalOnClass(HttpServletRequest.class)
    public RequestInterceptor feignCommonHeaderInterceptor() {
        return template -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            Optional.ofNullable(requestAttributes).ifPresent(attribute -> {
                HttpServletRequest request = attribute.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                Optional.ofNullable(headerNames).ifPresent(hn -> {
                    while (hn.hasMoreElements()) {
                        String headName = hn.nextElement();
                        if (commonHeaderList.contains(headName)) {
                            template.header(headName, request.getHeader(headName));
                        }
                    }
                });
            });
        };
    }
}
