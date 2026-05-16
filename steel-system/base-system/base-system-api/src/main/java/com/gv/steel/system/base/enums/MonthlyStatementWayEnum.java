package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum MonthlyStatementWayEnum implements BaseEnum<Integer> {
    MONTH_5_TH(0, "每月5号", (settlementDate) -> new LocalDate[]{settlementDate.plusMonths(-1).withDayOfMonth(5), settlementDate.withDayOfMonth(6)}),
    MONTH_10_TH(1, "每月10号", (settlementDate) -> new LocalDate[]{settlementDate.plusMonths(-1).withDayOfMonth(10), settlementDate.withDayOfMonth(11)}),
    MONTH_15_TH(2, "每月15号", (settlementDate) -> new LocalDate[]{settlementDate.plusMonths(-1).withDayOfMonth(15), settlementDate.withDayOfMonth(16)}),
    MONTH_20_TH(3, "每月20号", (settlementDate) -> new LocalDate[]{settlementDate.plusMonths(-1).withDayOfMonth(20), settlementDate.withDayOfMonth(21)}),
    MONTH_25_TH(4, "每月25号", (settlementDate) -> new LocalDate[]{settlementDate.plusMonths(-1).withDayOfMonth(25), settlementDate.withDayOfMonth(26)}),
    MONTH_LAST_DAY(5, "每月月底", (settlementDate) -> new LocalDate[]{settlementDate.with(TemporalAdjusters.firstDayOfMonth()), settlementDate.plusMonths(1).withDayOfMonth(1)}),
    ;

    private final Integer code;

    private final String desc;

    private final Function<LocalDate, LocalDate[]> statementDateMethod;

    @Override
    public Integer getParam() {
        return code;
    }

    public static MonthlyStatementWayEnum getInstance(Integer code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }
}
