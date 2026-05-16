package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReadFlagEnum implements BaseEnum<Integer> {
    UNREAD(0),
    READ(1),
    ;

    private final int readFlag;

    @Override
    public Integer getParam() {
        return readFlag;
    }
}

