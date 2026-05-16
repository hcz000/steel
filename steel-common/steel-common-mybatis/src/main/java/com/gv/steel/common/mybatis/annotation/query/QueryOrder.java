package com.gv.steel.common.mybatis.annotation.query;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface QueryOrder {
    /**
     * 排序方式，desc or asc
     */
    String value() default "";
}
