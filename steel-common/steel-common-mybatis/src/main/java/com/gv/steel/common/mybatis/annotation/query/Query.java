package com.gv.steel.common.mybatis.annotation.query;

import com.gv.steel.common.mybatis.enums.QueryType;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Query {
    String column() default "";

    QueryType expression() default QueryType.EQ;
}
