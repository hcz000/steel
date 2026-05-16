package com.gv.steel.common.redis.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatLimiter {
    /**
     * Spring EL expression
     */
    String value();

    /**
     * redis key
     */
    String key() default "";
}
