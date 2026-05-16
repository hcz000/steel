package com.gv.steel.system.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TaskNoticeStatusEnum {
    /**
     * 待办
     */
    PENDING(0, "待办"),
    /**
     * 审批通过
     */
    APPROVED(1, "审批通过"),
    /**
     * 审批不通过
     */
    APPROVAL_FAILED(2, "审批不通过"),
    /**
     * 作废
     */
    INVALID(3, "作废"),
    ;

    private final int status;

    private final String desc;

    public static String getDesc(int status) {
        return Arrays.stream(TaskNoticeStatusEnum.values())
                .filter(e -> e.getStatus() == status)
                .findFirst()
                .map(TaskNoticeStatusEnum::getDesc)
                .orElse(null);
    }
}
