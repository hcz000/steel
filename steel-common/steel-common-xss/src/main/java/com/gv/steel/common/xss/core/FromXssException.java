package com.gv.steel.common.xss.core;

import lombok.Getter;

/**
 * xss 表单异常
 */
@Getter
public class FromXssException extends IllegalStateException implements XssException {

    private static final long serialVersionUID = -7540670286699590592L;

    private final String input;

    public FromXssException(String input, String message) {
        super(message);
        this.input = input;
    }

}
