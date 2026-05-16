package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 加工收费的计重方式
 */
@Getter
@AllArgsConstructor
public enum ProcessingWeightWayEnum implements BaseEnum<Integer> {
    PROCESSING(0, "按原卷加工重计算加工费"),
    SELL(1, "按出货重计算加工费"),
    ;

    private final Integer code;

    private final String desc;


    @Override
    public Integer getParam() {
        return code;
    }
}
