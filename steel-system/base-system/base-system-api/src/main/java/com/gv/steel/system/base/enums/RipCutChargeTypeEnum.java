package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RipCutChargeTypeEnum implements BaseEnum<Integer> {
    RIP_CUT_PROCESS(0, "纵切收费"),
    RE_ROLL(1, "重卷");

    private final Integer code;

    private final String desc;

    @Override
    public Integer getParam() {
        return code;
    }
}
