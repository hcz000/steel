package com.gv.steel.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gv.steel.gateway.filter.*;
import com.gv.steel.gateway.handler.CaptchaImageHandler;
import com.gv.steel.gateway.handler.CheckCertValidHandler;
import com.gv.steel.gateway.handler.GlobalExceptionHandler;
import com.gv.steel.gateway.properties.GatewayConfigProperties;
import com.gv.steel.gateway.properties.SwaggerDocProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {

    /**
     * 前端密码解密过滤器
     */
    @Bean
    public PasswordDecoderFilter passwordDecoderFilter(GatewayConfigProperties configProperties) {
        return new PasswordDecoderFilter(configProperties);
    }

    /**
     * gateway api调用日志输出过滤器
     */
    @Bean
    public ApiLoggingFilter apiLoggingFilter() {
        return new ApiLoggingFilter();
    }

    /**
     * gateway全局request清洗过滤器
     */
    @Bean
    public RequestGlobalFilter requestGlobalFilter() {
        return new RequestGlobalFilter();
    }

    @Bean
    @ConditionalOnProperty(name = "swagger.basic.enabled")
    public SwaggerBasicGatewayFilter swaggerBasicGatewayFilter(
            SwaggerDocProperties swaggerProperties) {
        return new SwaggerBasicGatewayFilter(swaggerProperties);
    }

    /**
     * gateway 全局异常处理器
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }

    @Bean
    public ValidateCodeGatewayFilter validateCodeGatewayFilter(GatewayConfigProperties configProperties, ObjectMapper objectMapper) {
        return new ValidateCodeGatewayFilter(configProperties, objectMapper);
    }

    @Bean
    public CaptchaImageHandler captchaImageHandler() {
        return new CaptchaImageHandler();
    }

    @Bean
    public CheckCertValidHandler checkCertValidHandler() {
        return new CheckCertValidHandler();
    }
}
