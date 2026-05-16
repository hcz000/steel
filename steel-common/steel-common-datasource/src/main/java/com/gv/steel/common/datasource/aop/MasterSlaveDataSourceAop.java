package com.gv.steel.common.datasource.aop;

import com.gv.steel.common.datasource.annotation.ForcedMasterDB;
import com.gv.steel.common.datasource.context.MasterSlaveDataSourceContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.PatternMatchUtils;

import java.util.Arrays;

/**
 * 主从数据源切换切面
 */
@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)  // 在事务开始之前执行
public class MasterSlaveDataSourceAop {
    private static final String[] READ_RULE = new String[]{"select*", "list*", "page*", "get*", "query*", "search*", "count*", "detail*", "find*"};

    @Around("execution (* com.gv.steel..*.service..*.*(..))")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取当前请求的主从标识
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取方法上的注解、是否强制主库
            ForcedMasterDB forcedMasterDB = signature.getMethod().getAnnotation(ForcedMasterDB.class);
            // 获取事务方法上的注解
            Transactional transactional = signature.getMethod().getAnnotation(Transactional.class);
            // 获取事务类上的注解
            Transactional classTransactional = signature.getClass().getAnnotation(Transactional.class);
            // 获取方法名
            String methodName = signature.getMethod().getName();
            // 判断是否为读方法
            boolean isReadMethod = Arrays.stream(READ_RULE).anyMatch(ruleName -> PatternMatchUtils.simpleMatch(ruleName, methodName));
            if (forcedMasterDB != null) {
                MasterSlaveDataSourceContext.master(); // 强制主库
            }
            // 判断是否为只读方法
            else if ((transactional != null && transactional.readOnly())
                    || (classTransactional != null && classTransactional.readOnly())
                    || isReadMethod) {
                if (log.isDebugEnabled()) {
                    log.debug("标记为从库");
                }
                MasterSlaveDataSourceContext.slave();    // 只读，从库
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("标记为主库");
                }
                MasterSlaveDataSourceContext.master(); // 可写，主库
            }

            // 执行业务方法
            return joinPoint.proceed();

        } finally {
            MasterSlaveDataSourceContext.clean();
        }
    }
}
