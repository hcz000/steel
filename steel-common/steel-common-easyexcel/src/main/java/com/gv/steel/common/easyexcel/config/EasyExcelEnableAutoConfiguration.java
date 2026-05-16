package com.gv.steel.common.easyexcel.config;

import com.gv.steel.common.easyexcel.aspect.DynamicNameAspect;
import com.gv.steel.common.easyexcel.handler.EasyExcelExportReturnValueHandler;
import com.gv.steel.common.easyexcel.handler.I18nHeaderCellWriteHandler;
import com.gv.steel.common.easyexcel.processor.NameProcessor;
import com.gv.steel.common.easyexcel.processor.NameSPELExpressionProcessor;
import com.gv.steel.common.easyexcel.resolver.RequestExcelArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Import({EasyExcelHandlerAutoConfiguration.class})
public class EasyExcelEnableAutoConfiguration {
    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    private final EasyExcelExportReturnValueHandler easyExcelExportReturnValueHandler;

    /**
     * Excel名称解析处理切面
     *
     * @param nameProcessor SPEL 解析处理器
     * @return DynamicNameAspect
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
        return new DynamicNameAspect(nameProcessor);
    }

    /**
     * SPEL 解析处理器
     * @return NameProcessor excel名称解析器
     */
    @Bean
    @ConditionalOnMissingBean
    public NameProcessor nameProcessor() {
        return new NameSPELExpressionProcessor();
    }

    /**
     * excel 头的国际化处理器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(name = "exportHeadMessage")
    public I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler() {
        return new I18nHeaderCellWriteHandler();
    }

    /**
     * 追加 Excel返回值处理器 到 springmvc 中
     */
    @PostConstruct
    public void setReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = this.requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
        newHandlers.add(this.easyExcelExportReturnValueHandler);

        assert returnValueHandlers != null;

        newHandlers.addAll(returnValueHandlers);
        this.requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }

    /**
     * 追加 Excel 请求处理器 到 springmvc 中
     */
    @PostConstruct
    public void setRequestExcelArgumentResolver() {
        List<HandlerMethodArgumentResolver> argumentResolvers = this.requestMappingHandlerAdapter.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
        resolverList.add(new RequestExcelArgumentResolver());
        resolverList.addAll(argumentResolvers);
        this.requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
    }
}
