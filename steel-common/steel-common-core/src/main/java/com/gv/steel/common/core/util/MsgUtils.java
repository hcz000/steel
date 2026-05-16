package com.gv.steel.common.core.util;

import com.gv.steel.common.core.context.SpringContextHolder;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

/**
 * i18n 工具类
 */
@UtilityClass
public class MsgUtils {
    /**
     * 通过code 和参数获取错误信息
     *
     * @param code properties key
     * @param objects 占位符的值
     * @return properties value
     */
    public String getMessage(String code, Object... objects) {
        Locale locale = Optional.of(LocaleContextHolder.getLocale()).orElse(Locale.SIMPLIFIED_CHINESE);
        return getMessage(code, locale, objects);
    }

    /**
     * 通过code、参数和语言获取 系统 Exception 国际化信息
     *
     * @param code    properties key
     * @param objects 占位符的值
     * @return properties value
     */
    public String getSystemMessage(String code, Object... objects) {
        Locale locale = Optional.of(LocaleContextHolder.getLocale()).orElse(Locale.SIMPLIFIED_CHINESE);
        return getSystemMessage(code, locale, objects);
    }

    /**
     * 通过code、参数和语言获取Export Excel 表头国际化信息
     *
     * @param code    properties key
     * @param objects 占位符的值
     * @return properties value
     */
    public String getExportHeadMessage(String code, Object... objects) {
        Locale locale = Optional.of(LocaleContextHolder.getLocale()).orElse(Locale.SIMPLIFIED_CHINESE);
        return getExportHeadMessage(code, locale, objects);
    }

    /**
     * validate 通过code 和参数获取错误信息
     *
     * @param code properties key
     * @param objects 占位符的值
     * @return properties value
     */
    public String getValidateMessage(String code, Object... objects) {
        Locale locale = Optional.of(LocaleContextHolder.getLocale()).orElse(Locale.SIMPLIFIED_CHINESE);
        return getValidateMessageSource(code, locale, objects);
    }

    /**
     * security 通过code 和参数获取错误信息
     *
     * @param code properties key
     * @param objects 占位符的值
     * @return properties value
     */
    public String getSecurityMessage(String code, Object... objects) {
        Locale locale = Optional.of(LocaleContextHolder.getLocale()).orElse(Locale.SIMPLIFIED_CHINESE);
        return getSecurityMessage(code, locale, objects);
    }

    /**
     * 通过code、参数和语言获取错误信息
     *
     * @param code properties key
     * @param locale 国际化
     * @param objects 占位符的值
     * @return properties value
     */
    public String getMessage(String code, Locale locale, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("messageSource");
        return messageSource.getMessage(code, objects, locale);
    }

    /**
     * 通过code、参数和语言获取系统默认的国际化信息
     *
     * @param code    properties key
     * @param locale  国际化
     * @param objects 占位符的值
     * @return properties value
     */
    public String getSystemMessage(String code, Locale locale, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("systemMessageSource");
        return messageSource.getMessage(code, objects, locale);
    }

    /**
     * 通过code、参数和语言获取Export Excel 表头国际化信息
     *
     * @param code    properties key
     * @param locale  国际化
     * @param objects 占位符的值
     * @return properties value
     */
    public String getExportHeadMessage(String code, Locale locale, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("exportHeadMessageSource");
        return messageSource.getMessage(code, objects, locale);
    }

    /**
     * method param validate 通过code、参数和语言获取错误信息
     *
     * @param code properties key
     * @param locale 国际化
     * @param objects 占位符的值
     * @return properties value
     */
    public String getValidateMessageSource(String code, Locale locale, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("validateMessageSource");
        return messageSource.getMessage(code, objects, locale);
    }

    /**
     * security 通过code、参数和语言获取中文错误信息
     *
     * @param code properties key
     * @param locale 国际化
     * @param objects 占位符的值
     * @return properties value
     */
    public String getSecurityMessage(String code, Locale locale, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("securityMessageSource");
        return messageSource.getMessage(code, objects, locale);
    }

}
