package com.gv.steel.common.easyexcel.aspect;

import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;
import com.gv.steel.common.easyexcel.processor.NameProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
public class DynamicNameAspect {
    public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";
    private final NameProcessor processor;

    @Before("@annotation(easyExcelExport)")
    public void before(JoinPoint point, EasyExcelExport easyExcelExport) {
        MethodSignature ms = (MethodSignature) point.getSignature();
        String name = easyExcelExport.name();
        if (!StringUtils.hasText(name)) {
            name = LocalDateTime.now().toString();
        } else {
            name = this.processor.doDetermineName(point.getArgs(), ms.getMethod(), easyExcelExport.name());
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes).setAttribute(EXCEL_NAME_KEY, name, 0);
    }

    public DynamicNameAspect(final NameProcessor processor) {
        this.processor = processor;
    }
}
