package com.gv.steel.common.datasource.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 主从数据源上下文
 */
public class MasterSlaveDataSourceContext {
    /**
     * 主从数据源标识，TRUE OR NULL:主数据源，FALSE:从数据源
     */
    private static final ThreadLocal<Boolean> MASTER_SLAVE_FLAG = new TransmittableThreadLocal<>();

    /**
     * 返回标记
     *
     * @return TRUE OR NULL:主数据源，FALSE:从数据源
     */
    public static Boolean get() {
        return MASTER_SLAVE_FLAG.get();
    }

    /**
     * 写状态，标记为主库
     */
    public static void master() {
        MASTER_SLAVE_FLAG.set(Boolean.TRUE);
    }

    /**
     * 读状态，标记为从库
     */
    public static void slave() {
        MASTER_SLAVE_FLAG.set(Boolean.FALSE);
    }

    /**
     * 清空标记
     */
    public static void clean() {
        MASTER_SLAVE_FLAG.remove();
    }
}
