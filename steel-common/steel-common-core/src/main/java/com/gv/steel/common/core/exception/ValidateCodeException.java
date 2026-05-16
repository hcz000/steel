package com.gv.steel.common.core.exception;

/**
 * 验证码异常
 */
public class ValidateCodeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ValidateCodeException() {
    }

    public ValidateCodeException(String message) {
        super(message);
    }
}
