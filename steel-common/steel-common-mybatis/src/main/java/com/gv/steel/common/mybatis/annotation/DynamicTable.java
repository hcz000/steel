package com.gv.steel.common.mybatis.annotation;


import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicTable {
    /**
     * 动态表名
     *
     * @return {String}
     */
    String value() default "";

    /**
     * 提取分表参数的SPRING-EL表达式
     *
     * @return {String}
     */
    String expression() default "";
}
