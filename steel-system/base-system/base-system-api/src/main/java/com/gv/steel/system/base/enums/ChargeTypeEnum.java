package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChargeTypeEnum implements BaseEnum<Integer> {
    INSUFFICIENT(0, "不足"),
    ACCORD(1, "符合"),
    EXCEED(2, "超出"),
    ;
    private final Integer code;

    private final String desc;

    @Override
    public Integer getParam() {
        return code;
    }
}
