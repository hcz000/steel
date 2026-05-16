package com.gv.steel.common.feign;

import com.gv.steel.common.feign.aspect.FeignRetryAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * 重试配置
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RetryTemplate.class)
public class CustomFeignRetryAutoConfiguration {

    @Bean
    public FeignRetryAspect feignRetryAspect() {
        return new FeignRetryAspect();
    }

}
