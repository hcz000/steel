package com.gv.steel.common.mybatis.enums;

import com.gv.steel.common.mybatis.strategy.AbstractDataColumnStrategy;
import com.gv.steel.common.mybatis.strategy.UserDataColumnStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ColumnType {
    /**
     * 仅创建人可访问数据
     */
    USER(UserDataColumnStrategy.class),
//    DEPT(),
//    FACTORY(),
    ;

    private final Class<? extends AbstractDataColumnStrategy> clazz;
}
