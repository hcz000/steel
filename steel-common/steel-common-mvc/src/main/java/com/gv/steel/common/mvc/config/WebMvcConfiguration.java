package com.gv.steel.common.mvc.config;

import cn.hutool.core.date.DatePattern;
import com.gv.steel.common.core.factory.YamlPropertySourceFactory;
import com.gv.steel.common.core.format.SpecFormatAnnotationFormatterFactory;
import com.gv.steel.common.core.format.SpecFormatter;
import com.gv.steel.common.core.jackson.Java8TimeModule;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * web mv configuration
 */
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = SERVLET)
@PropertySource(factory = YamlPropertySourceFactory.class, value = {"classpath:mvc.yml"})
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final SpecFormatter specFormatter;

    /**
     * 增加GET请求参数中时间类型转换 {@link Java8TimeModule}
     * <ul>
     * <li>HH:mm:ss -> LocalTime</li>
     * <li>yyyy-MM-dd -> LocalDate</li>
     * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
     * </ul>
     *
     * @param registry
     */
    @Override
    public void addFormatters(@NotNull FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DatePattern.NORM_TIME_FORMATTER);
        registrar.setDateFormatter(DatePattern.NORM_DATE_FORMATTER);
        registrar.setDateTimeFormatter(DatePattern.NORM_DATETIME_FORMATTER);
        registrar.registerFormatters(registry);

        // 自定义规格格式化，支持注解@SpecFormat 以及 参数
        registry.addFormatterForFieldAnnotation(new SpecFormatAnnotationFormatterFactory());
        registry.addFormatter(specFormatter);
    }

    @Bean
    @ConditionalOnMissingBean({AcceptHeaderLocaleResolver.class})
    public LocaleResolver aceptHeaderLocaleResolver() {
        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return acceptHeaderLocaleResolver;
    }

}
