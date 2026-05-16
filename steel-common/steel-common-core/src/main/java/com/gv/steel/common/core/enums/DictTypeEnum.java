package com.gv.steel.common.core.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DictTypeEnum implements BaseEnum<Integer> {

    /**
     * 字典类型-系统内置（不可修改）
     */
    SYSTEM(1, "系统内置"),

    /**
     * 字典类型-业务类型
     */
    BIZ(0, "业务类");

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
