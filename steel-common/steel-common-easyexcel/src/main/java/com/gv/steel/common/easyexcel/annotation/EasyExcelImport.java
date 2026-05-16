package com.gv.steel.common.easyexcel.annotation;

import com.gv.steel.common.easyexcel.listener.DefaultAnalysisEventListener;
import com.gv.steel.common.easyexcel.listener.ListAnalysisEventListener;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyExcelImport {
    String fileName() default "file";

    Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

    boolean ignoreEmptyRow() default false;
}
