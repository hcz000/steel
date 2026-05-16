package com.gv.steel.common.core.validator;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.gv.steel.common.core.annotation.CheckDateRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;

public class CheckDateRangeValidator implements ConstraintValidator<CheckDateRange, LocalDate[]> {

    private CheckDateRange checkDateRange;


    @Override
    public void initialize(CheckDateRange constraintAnnotation) {
        this.checkDateRange = constraintAnnotation;
    }

    @Override
    public boolean isValid(LocalDate[] value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.length != 2) {
            return false;
        }

        long days = LocalDateTimeUtil.between(value[0].atTime(LocalTime.MIN), value[1].atTime(LocalTime.MAX)).toDays();

        return days <= checkDateRange.value();
    }
}
