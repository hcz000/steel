package com.gv.steel.common.core.util;

import cn.hutool.core.util.StrUtil;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.exception.BaseException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Slf4j
@UtilityClass
public class LanguageUtils {
    public Locale getLanguage(HttpServletRequest request) {
        String lang = request.getHeader(CommonConstants.HEADER_LANG);
        return getLanguage(lang);
    }

    public Locale getLanguage(String lang) {
        if (StrUtil.isNotBlank(lang)) {
            try {
                if (lang.contains("-")) {
                    String[] split = lang.split("-");
                    return new Locale(split[0], split[1]);
                }
                return new Locale(lang);
            } catch (Exception e) {
                log.error("语言获取异常, language: {}, e: ", lang, e);
                throw new BaseException();
            }
        }

        return Locale.SIMPLIFIED_CHINESE;
    }
}
