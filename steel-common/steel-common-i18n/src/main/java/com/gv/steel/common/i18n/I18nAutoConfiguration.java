package com.gv.steel.common.i18n;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.gv.steel.common.i18n.source.NacosBundleMessageSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Locale;
import java.util.concurrent.Executor;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
public class I18nAutoConfiguration {

    private final NacosConfigManager nacosConfigManager;

    private final ConfigurableEnvironment environment;

    private final Executor asyncExecutor;

    /**
     * 业务国际化文件配置
     *
     * @return MessageSource
     */
    @Bean
    public MessageSource messageSource() {
        NacosBundleMessageSource messageSource = new NacosBundleMessageSource(nacosConfigManager, asyncExecutor);
        String applicationName = environment.getProperty("spring.application.name");
        String basename = "classpath:i18n/message/" + (StrUtil.isNotBlank(applicationName) ? applicationName : "application");
        messageSource.setBasename(basename.replace("-", "_"));
        messageSource.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return messageSource;
    }

    /**
     * 系统默认的国际化文件配置
     *
     * @return MessageSource
     */
    @Bean
    public MessageSource systemMessageSource() {
        NacosBundleMessageSource messageSource = new NacosBundleMessageSource(nacosConfigManager, asyncExecutor);
        messageSource.setBasename("classpath:i18n/system/messages");
        messageSource.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return messageSource;
    }

    /**
     * 参数校验国际化文件配置
     *
     * @return MessageSource
     */
    @Bean
    public MessageSource validateMessageSource() {
        NacosBundleMessageSource messageSource = new NacosBundleMessageSource(nacosConfigManager, asyncExecutor);
        String applicationName = environment.getProperty("spring.application.name");
        String basename = "classpath:i18n/validate/" + (StrUtil.isNotBlank(applicationName) ? applicationName : "application");
        messageSource.setBasename(basename.replace("-", "_"));
        messageSource.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return messageSource;
    }

    /**
     * 导出excel 头国际化文件配置
     *
     * @return MessageSource
     */
    @Bean
    public MessageSource exportHeadMessageSource() {
        NacosBundleMessageSource messageSource = new NacosBundleMessageSource(nacosConfigManager, asyncExecutor);
        String applicationName = environment.getProperty("spring.application.name");
        String basename = "classpath:i18n/export/" + (StrUtil.isNotBlank(applicationName) ? applicationName : "application");
        messageSource.setBasename(basename.replace("-", "_"));
        messageSource.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return messageSource;
    }

    @Bean
    @ConditionalOnWebApplication(type = SERVLET)
    public MessageSource securityMessageSource() {
        NacosBundleMessageSource messageSource = new NacosBundleMessageSource(nacosConfigManager, asyncExecutor);
        messageSource.addBasenames("classpath:i18n/security/messages");
        messageSource.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return messageSource;
    }
}
