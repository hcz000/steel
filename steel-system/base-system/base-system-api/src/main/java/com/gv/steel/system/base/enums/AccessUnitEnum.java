package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessUnitEnum implements BaseEnum<Integer> {
    YUAN_EACH(0, "元/个"),
    YUAN_PER_TON(1, "元/吨");

    private final Integer code;

    private final String desc;

    @Override
    public Integer getParam() {
        return code;
    }
}
