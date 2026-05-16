package com.gv.steel.system.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerTypeEnum {
    /**
     * 委托单位
     */
    DELEGATE(0),
    /**
     * 贸易客户
     */
    TRADE(1);
    private final int type;

}
