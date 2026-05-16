package com.gv.steel.common.mybatis.annotation;

import com.gv.steel.common.mybatis.enums.ColumnType;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(DataScope.class)
public @interface DataColumn {
    String alias() default "";

    String name();

    ColumnType type() default ColumnType.USER;
}
