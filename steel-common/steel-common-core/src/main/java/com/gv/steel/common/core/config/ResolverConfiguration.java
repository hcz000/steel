package com.gv.steel.common.core.config;

import com.google.common.collect.Lists;
import com.gv.steel.common.core.resolver.LanguageArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnBean(RequestMappingHandlerAdapter.class)
public class ResolverConfiguration implements InitializingBean {
    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> customArgumentResolvers = Lists.newArrayList();

        assert argumentResolvers != null;
        for (HandlerMethodArgumentResolver argumentResolver : argumentResolvers) {
            if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
                customArgumentResolvers.add(new LanguageArgumentResolver());
            }
            customArgumentResolvers.add(argumentResolver);
        }

        requestMappingHandlerAdapter.setArgumentResolvers(customArgumentResolvers);
    }
}
