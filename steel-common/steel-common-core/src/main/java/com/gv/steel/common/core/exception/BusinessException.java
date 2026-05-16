package com.gv.steel.common.core.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 4696103024272840917L;

    public BusinessException(String message) {
        super(message);
    }
}
