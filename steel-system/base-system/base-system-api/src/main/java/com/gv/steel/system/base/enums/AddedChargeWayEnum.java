package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddedChargeWayEnum implements BaseEnum<Integer> {
    COUNT(0, "按数量"),
    SCALE(1, "按比例"),
    FIXED(2, "按固定");

    private final Integer code;

    private final String desc;

    @Override
    public Integer getParam() {
        return code;
    }
}
