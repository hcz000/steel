package com.gv.steel.common.log.util;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 日志类型
 */
@Getter
@RequiredArgsConstructor
public enum LogTypeEnum implements BaseEnum<Integer> {

    /**
     * 正常日志类型
     */
    NORMAL(0, "正常日志"),

    /**
     * 错误日志类型
     */
    ERROR(0, "错误日志");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String description;


    @Override
    public Integer getParam() {
        return type;
    }
}
