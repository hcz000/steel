package com.gv.steel.common.redis.aspect;

import cn.hutool.core.util.StrUtil;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.lock.CommonLock;
import com.gv.steel.common.core.lock.DistributedLock;
import com.gv.steel.common.core.util.ELUtil;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.redis.annotation.RepeatLimiter;
import com.gv.steel.common.redis.constant.RepeatLimitConstant;
import com.gv.steel.common.redis.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class RepeatLimitAspect {

    private final DistributedLock distributedLock;

    @Around("@annotation(repeatLimiter)")
    public Object pointcut(ProceedingJoinPoint point, RepeatLimiter repeatLimiter) throws Throwable {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

        String expression = repeatLimiter.value();
        // 当前表达式存在 SPEL，会覆盖 value 的值
        if (StrUtil.isNotBlank(expression)) {
            // 解析SPEL
            MethodSignature signature = (MethodSignature) point.getSignature();
            EvaluationContext context = ELUtil.getContext(point.getArgs(), signature.getMethod());
            String value;
            try {
                value = ELUtil.getValue(context, expression, String.class);
            } catch (Exception e) {
                // SPEL 表达式异常，获取 value 的值
                log.error("解析SPEL {} 异常， error:", expression, e);
                throw new BaseException();
            }

            if (StrUtil.isBlank(value)) {
                throw new BaseException(MsgUtils.getSystemMessage("request.unique.param"));
            }

            String key = repeatLimiter.key();

            String redisKey = RepeatLimitConstant.REDIS_LIMIT_KEY_PREFIX + (StrUtil.isNotBlank(key) ? key + RepeatLimitConstant.SEPARATOR : "") + value;
            try (CommonLock commonLock = distributedLock.tryLock(redisKey, 10L, TimeUnit.SECONDS)) {
                if (commonLock == null) {
                    log.error("@RepeatLimiter 重复提交验证获取锁超时");
                    throw new BaseException();
                }

                if (!RedisUtil.hasKey(redisKey)) {
                    throw new BaseException(MsgUtils.getSystemMessage("repeat.submit"));
                }

                Object proceed = point.proceed();
                RedisUtil.deleteObject(redisKey);
                return proceed;
            }
        }

        return point.proceed();
    }
}
