package com.gv.steel.common.core.exception;

import cn.hutool.core.util.ObjectUtil;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.ErrorCodeConstants;
import com.gv.steel.common.core.util.MsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DefaultExceptionAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public Result<?> badRequestException(IllegalArgumentException e) {
        return this.defHandler(e.getMessage(), e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public Result<?> badMethodExpressException(AccessDeniedException e) {
        return this.defHandler(MsgUtils.getSystemMessage(ErrorCodeConstants.SERVER_ACCESS_DENIED), e);
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Result<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return this.defHandler(MsgUtils.getSystemMessage(ErrorCodeConstants.HTTP_MEDIA_TYPE_NOT_SUPPORTED), e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public Result<?> handleSQLException(SQLException e) {
        return this.defHandler(MsgUtils.getSystemMessage(ErrorCodeConstants.SQL_EXCEPTION), e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({BusinessException.class})
    public Result<?> handleException(BusinessException e) {
        return this.defHandler(e.getMessage(), e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({BaseException.class})
    public Result<?> handleException(BaseException e) {
        return this.defHandler(e.getMessage(), e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> noHandlerFoundException(NoHandlerFoundException e) {
        return this.defHandler(MsgUtils.getSystemMessage(ErrorCodeConstants.HTTP_NOT_FOUND), e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public Result<?> exceptionHandler(Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        if (e instanceof MethodArgumentNotValidException) {
            BindingResult result = ((MethodArgumentNotValidException) e).getBindingResult();
            if (result.hasErrors()) {
                List<ObjectError> errors = result.getAllErrors();
                if (ObjectUtil.isNotNull(errors)) {
                    errors.forEach((p) -> {
                        FieldError fieldError = (FieldError) p;
                        if (ObjectUtil.isNotEmpty(stringBuilder)) {
                            stringBuilder.append(";");
                        }

                        stringBuilder.append(fieldError.getDefaultMessage());
                    });
                }
            }
        } else if (e instanceof HttpMessageNotReadableException) {
            Throwable error = ((HttpMessageNotReadableException) e).getMostSpecificCause();
            if (error instanceof BaseException) {
                stringBuilder.append(error.getMessage());
            } else {
                return defHandler(MsgUtils.getSystemMessage(ErrorCodeConstants.REQUIRED_PARAMETER_MISSING, e.getMessage()), e);
            }
        }


        return Result.failed(stringBuilder.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class, ValidationException.class})
    public Result<?> handleValidatedException(Exception e) {
        Result<?> resp = null;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            resp = Result.failed(HttpStatus.BAD_REQUEST.value(), ex.getBindingResult().getAllErrors().parallelStream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; ")));
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e;
            resp = Result.failed(HttpStatus.BAD_REQUEST.value(), ex.getConstraintViolations().parallelStream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; ")));
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            resp = Result.failed(HttpStatus.BAD_REQUEST.value(), ex.getAllErrors().parallelStream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; ")));
        }

        return resp;
    }

    public Result<?> defHandler(String msg, Exception e) {
        log.error(msg, e);
        return Result.failed(msg);
    }
}
