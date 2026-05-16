package com.gv.steel.common.easyexcel.processor;

import java.lang.reflect.Method;

/**
 * 参数名称处理器
 */
public interface NameProcessor {
    String doDetermineName(Object[] args, Method method, String key);
}
