package com.gv.steel.gateway.config;

import com.gv.steel.gateway.handler.CaptchaImageHandler;
import com.gv.steel.gateway.handler.CheckCertValidHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 路由配置信息
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class RouterFunctionConfiguration {

    private final CaptchaImageHandler captchaImageHandler;

    private final CheckCertValidHandler checkCertValidHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route(
                        RequestPredicates.path("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), captchaImageHandler)
                .andRoute(RequestPredicates.path("/checkCertValid").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), checkCertValidHandler);
    }

}
