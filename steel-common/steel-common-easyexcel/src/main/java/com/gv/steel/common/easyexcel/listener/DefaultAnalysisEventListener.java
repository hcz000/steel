package com.gv.steel.common.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.gv.steel.common.easyexcel.annotation.ExcelLine;
import com.gv.steel.common.easyexcel.model.ErrorMessage;
import com.gv.steel.common.easyexcel.resolver.Validators;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * default excel analysis listener
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {
    private final AtomicLong lineNum = new AtomicLong(1L);

    private final List<Object> list = Lists.newArrayList();

    private final List<ErrorMessage> errorMessages = Lists.newArrayList();

    @Override
    public List<Object> getList() {
        return list;
    }

    @Override
    public List<ErrorMessage> getErrorMessage() {
        return errorMessages;
    }

    @Override
    public void invoke(Object data, AnalysisContext context) {
        long line = lineNum.addAndGet(1L);
        Set<ConstraintViolation<Object>> violations = Validators.validate(data);
        if (!violations.isEmpty()) {
            Set<String> messageSet = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
            this.errorMessages.add(new ErrorMessage(line, messageSet));
        } else {
            Field[] fields = data.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelLine.class) && field.getType() == Long.class) {
                    try {
                        field.setAccessible(true);
                        field.set(data, line);
                    } catch (IllegalAccessException e) {
                        log.error("reflect set line num error !!");
                    }
                }
            }
            this.list.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    public void setLineNum(Long lineNum) {
        this.lineNum.set(lineNum);
    }
}
