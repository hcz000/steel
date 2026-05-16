package com.gv.steel.common.easyexcel.annotation;

import com.gv.steel.common.easyexcel.model.HeadGenerator;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {
    int sheetNo() default -1;

    String sheetName();

    String[] includes() default {};

    String[] excludes() default {};

    Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;
}
