package com.gv.steel.common.feign.sentinel.handler;

import com.alibaba.csp.sentinel.Tracer;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.constant.ErrorCodeConstants;
import com.gv.steel.common.core.util.MsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 全局异常处理器结合sentinel 全局异常处理器不能作用在 oauth server
 * </p>
 */
@Slf4j
@Order(10001)
@RestControllerAdvice
@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
public class GlobalBizExceptionHandler {

    /**
     * 全局异常.
     *
     * @param e the e
     * @return Result
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleGlobalException(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        // 业务异常交由 sentinel 记录
        Tracer.trace(e);
        return Result.failed(MsgUtils.getSystemMessage(ErrorCodeConstants.DEFAULT_EXCEPTION));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleAccessDeniedException(AccessDeniedException e) {
        String msg = MsgUtils.getSecurityMessage("AbstractAccessDecisionManager.accessDenied");
        log.warn("拒绝授权异常信息 ex={}", e.getLocalizedMessage());
        Result<Object> result = Result.failed(msg);
        result.setCode(CommonConstants.UNAUTHORIZED);
        return result;
    }

}
