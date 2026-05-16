package com.gv.steel.system.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConsumerTypeEnum {
    REDIS("redis"),
    ROCKETMQ("rocketmq"),
    ;
    private final String type;
}
