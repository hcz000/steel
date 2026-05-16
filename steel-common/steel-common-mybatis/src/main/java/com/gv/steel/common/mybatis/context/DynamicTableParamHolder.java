package com.gv.steel.common.mybatis.context;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

public class DynamicTableParamHolder {

    /**
     * 动态表参数
     */
    private static final ThreadLocal<ConcurrentMap<String, Object>> LOCAL_DYNAMIC_TABLE_CONTEXT = new TransmittableThreadLocal<>();

    private DynamicTableParamHolder() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        ConcurrentMap<String, Object> stringObjectConcurrentMap = LOCAL_DYNAMIC_TABLE_CONTEXT.get();
        if (CollUtil.isEmpty(stringObjectConcurrentMap)) {
            return null;
        }
        return (T) stringObjectConcurrentMap.get(key);
    }

    public static <T> void set(String key, T value) {
        ConcurrentMap<String, Object> stringObjectConcurrentMap = LOCAL_DYNAMIC_TABLE_CONTEXT.get();
        if (CollUtil.isEmpty(stringObjectConcurrentMap)) {
            stringObjectConcurrentMap = Maps.newConcurrentMap();
        }
        stringObjectConcurrentMap.put(key, value);
        LOCAL_DYNAMIC_TABLE_CONTEXT.set(stringObjectConcurrentMap);
    }

    public static void clear() {
        LOCAL_DYNAMIC_TABLE_CONTEXT.remove();
    }
}
