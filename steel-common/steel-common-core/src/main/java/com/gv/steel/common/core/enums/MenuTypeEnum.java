package com.gv.steel.common.core.enums;

import com.gv.steel.common.core.enums.base.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 菜单类型
 */
@Getter
@RequiredArgsConstructor
public enum MenuTypeEnum implements BaseEnum<Integer> {

    /**
     * 目录
     */
    DIRECTOR(0, "director"),

    /**
     * 菜单
     */
    MENU(1, "menu"),

    /**
     * 按钮
     */
    BUTTON(2, "button");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String description;


    @Override
    public Integer getParam() {
        return type;
    }
}
