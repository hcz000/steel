package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessUsedScopeEnum implements BaseEnum<Integer> {
    WHOLE_ORDER_USED(0, "整单适用"),
    SINGLE_USED(1, "单件适用"),
    BALED_USED(2, "打包适用"),
    ;

    private final Integer code;

    private final String desc;

    @Override
    public Integer getParam() {
        return code;
    }
}
