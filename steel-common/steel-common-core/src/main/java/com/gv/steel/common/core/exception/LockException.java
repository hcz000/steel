package com.gv.steel.common.core.exception;

/**
 * 锁异常
 */
public class LockException extends RuntimeException {

    private static final long serialVersionUID = 763794653134071044L;

    public LockException(String message) {
        super(message);
    }
}
