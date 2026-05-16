package com.gv.steel.common.mvc.advice;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.ErrorCodeConstants;
import com.gv.steel.common.core.exception.DefaultExceptionAdvice;
import com.gv.steel.common.core.util.MsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Arrays;

@Slf4j
@Order(10000)
@RestControllerAdvice
public class WebMvcExceptionAdvice extends DefaultExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result<?> requestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        String[] supportedMethods = exception.getSupportedMethods();
        return Result.failed(MsgUtils.getSystemMessage(ErrorCodeConstants.REQUEST_METHOD_EXCEPTION, Arrays.toString(supportedMethods)));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MissingServletRequestPartException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public Result<?> missingServletRequestParameterException(Exception exception) {
        String parameterName = exception instanceof MissingServletRequestParameterException ? ((MissingServletRequestParameterException) exception).getParameterName() : (exception instanceof MissingServletRequestPartException ? ((MissingServletRequestPartException) exception).getRequestPartName() : "null");
        return Result.failed(MsgUtils.getSystemMessage(ErrorCodeConstants.REQUIRED_PARAMETER_MISSING, parameterName));
    }
}
