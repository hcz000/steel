package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NoticePublishStatusEnum implements BaseEnum<Integer> {
    UN_PUBLISH(0),
    PUBLISHED(1);

    private final int status;

    @Override
    public Integer getParam() {
        return status;
    }
}
