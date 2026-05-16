package com.gv.steel.common.core.api;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.constant.ErrorCodeConstants;
import com.gv.steel.common.core.util.MsgUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -7274925035099653745L;

    // 成功标识
    private boolean success;
    // 数据项
    private T data;
    // 响应码
    private Integer code;
    // 响应描述
    private String msg;

    public static <T> Result<T> ok() {
        return restResult(null, CommonConstants.SUCCESS, MsgUtils.getSystemMessage(ErrorCodeConstants.DEFAULT_SUCCESS_MSG), true);
    }

    public static <T> Result<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, MsgUtils.getSystemMessage(ErrorCodeConstants.DEFAULT_SUCCESS_MSG), true);
    }

    public static <T> Result<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.SUCCESS, msg, true);
    }

    public static <T> Result<T> failed() {
        return restResult(null, CommonConstants.FAIL, MsgUtils.getSystemMessage(ErrorCodeConstants.DEFAULT_FAIL_MSG), false);
    }

    public static <T> Result<T> failed(String msg) {
        return restResult(null, CommonConstants.FAIL, msg, false);
    }

    public static <T> Result<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, MsgUtils.getSystemMessage(ErrorCodeConstants.DEFAULT_FAIL_MSG), false);
    }

    public static <T> Result<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg, false);
    }

    public static Result<Boolean> operationResult(boolean result) {
        return restResult(result, result ? CommonConstants.SUCCESS : CommonConstants.FAIL, result ? MsgUtils.getSystemMessage(ErrorCodeConstants.DEFAULT_SUCCESS_MSG) : MsgUtils.getSystemMessage(ErrorCodeConstants.DEFAULT_FAIL_MSG), result);
    }

    public static <T> Result<T> result(T data, int code, String msg, boolean success) {
        return restResult(data, code, msg, success);
    }

    public static <T> Result<T> restResult(T data, int code, String msg, boolean success) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setSuccess(success);
        return apiResult;
    }

    public Boolean isSuccess() {
        return (this.code == CommonConstants.SUCCESS);
    }
}
