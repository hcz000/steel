package com.gv.steel.common.mybatis.annotation.query;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface QuerySort {
    /**
     * 排序字段，字段名，驼峰或“_”
     */
    String value() default "";
}
