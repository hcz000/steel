package com.gv.steel.common.mybatis.annotation.query;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SelectColumn {
    /**
     * 需查询的字段项，为空查所有
     */
    String[] value();
}
