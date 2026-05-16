package com.gv.steel.common.rocketmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MQ 延迟消息 延迟等级
 * 默认值为“1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h”，18个level
 */
@Getter
@AllArgsConstructor
public enum DelayLevelEnum {
    NO_DELAY(0),
    DELAY_1_SECONDS(1),
    DELAY_5_SECONDS(2),
    DELAY_10_SECONDS(3),
    DELAY_30_SECONDS(4),
    DELAY_1_MINUTES(5),
    DELAY_2_MINUTES(6),
    DELAY_3_MINUTES(7),
    DELAY_4_MINUTES(8),
    DELAY_5_MINUTES(9),
    DELAY_6_MINUTES(10),
    DELAY_7_MINUTES(11),
    DELAY_8_MINUTES(12),
    DELAY_9_MINUTES(13),
    DELAY_10_MINUTES(14),
    DELAY_20_MINUTES(15),
    DELAY_30_MINUTES(16),
    DELAY_1_HOURS(17),
    DELAY_2_HOURS(18),
    ;

    private final int level;
}
