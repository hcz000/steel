package com.gv.steel.common.core.annotation;

import com.gv.steel.common.core.validator.CheckDateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckDateRangeValidator.class)
public @interface CheckDateRange {

    String message() default "日期范围不能超过{value}";

    int value() default 31;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
