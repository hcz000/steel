package com.gv.steel.common.datasource.annotation;


import java.lang.annotation.*;

/**
 * 强制使用主库
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForcedMasterDB {
}
