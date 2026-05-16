package com.gv.steel.system.base.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FolderFileEnum implements BaseEnum<Integer> {
    /**
     * 文件夹
     */
    FOLDER(0),
    /**
     * 文件
     */
    FILE(1),
    ;

    private final int type;

    @Override
    public Integer getParam() {
        return type;
    }
}
