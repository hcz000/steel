package com.gv.steel.common.mybatis.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gv.steel.common.core.util.ELUtil;
import com.gv.steel.common.mybatis.annotation.DynamicTable;
import com.gv.steel.common.mybatis.context.DynamicTableParamHolder;
import com.gv.steel.common.mybatis.properties.DynamicTableProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

/**
 * 动态表名请求参数提取切片
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class DynamicTableParamAop {
    private final DynamicTableProperties properties;

    @SneakyThrows
    @Around("@annotation(dynamicTable)")
    public Object handle(ProceedingJoinPoint point, DynamicTable dynamicTable) {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

        String tableName = dynamicTable.value();
        String expression = dynamicTable.expression();

        if (StrUtil.isBlank(tableName) || StrUtil.isBlank(expression)) {
            throw new RuntimeException("请配置表名参数提取表达式");
        }

        try {
            // 解析SPEL
            MethodSignature signature = (MethodSignature) point.getSignature();
            EvaluationContext context = ELUtil.getContext(point.getArgs(), signature.getMethod());
            Object param = ELUtil.getValue(context, expression, Object.class);
            // 如果是id-hash
            if (CollUtil.isNotEmpty(properties.getIdHashDynamicTable())
                    && properties.getIdHashDynamicTable().containsKey(tableName)) {
                DynamicTableProperties.IdHashDynamicTableProperties idProperties = properties.getIdHashDynamicTable().get(tableName);
                DynamicTableParamHolder.set(idProperties.getIdDateParam(), param);
            }

            // 如果是按月份分表
            if (CollUtil.isNotEmpty(properties.getMonthDateDynamicTable())
                    && properties.getMonthDateDynamicTable().containsKey(tableName)) {
                DynamicTableProperties.MonthDateDynamicTableProperties monthDateProperties = properties.getMonthDateDynamicTable().get(tableName);
                DynamicTableParamHolder.set(monthDateProperties.getMonthDateParam(), param);
            }
            return point.proceed();
        } catch (Exception e) {
            log.error("获取分表参数失败", e);
            return point.proceed();
        } finally {
            DynamicTableParamHolder.clear();
        }
    }

}
