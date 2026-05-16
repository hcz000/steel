package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 辅料收费方式枚举
 */
@Getter
@AllArgsConstructor
public enum AccessoryChargeWayEnum implements BaseEnum<Integer> {
    ACCESSORY_QUANTITY(0, "按辅料数量"),
    PRODUCT_WEIGHT(1, "按成品重量"),
    RAW_WEIGHT(2, "按原卷重量"),
    ;

    private final Integer code;

    private final String desc;

    @Override
    public Integer getParam() {
        return code;
    }
}
