package com.gv.steel.common.datasource.datasource;

import com.gv.steel.common.datasource.context.MasterSlaveDataSourceContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Getter
public class MasterSlaveDataSource extends AbstractRoutingDataSource {

    /**
     * 从库的 Key 列表
     */
    @Setter
    private List<String> slaveKeys;

    /**
     * 从库 key 列表的索引
     */
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    protected Object determineCurrentLookupKey() {
        // 是否是主库
        Boolean masterFlag = MasterSlaveDataSourceContext.get();

        // 没有从库, 或是主库
        if (masterFlag == null || masterFlag || slaveKeys == null || slaveKeys.isEmpty()) {
            // 直接返回 null，让spring默认使用主库
            if (log.isDebugEnabled()) {
                log.debug("当前使用主库");
            }
            return null;
        }

        // 轮询从库
        int index = this.index.getAndIncrement() % slaveKeys.size();
        if (this.index.get() == Integer.MAX_VALUE) {
            this.index.set(0);
        }

        String slaveKey = this.slaveKeys.get(index);
        if (log.isDebugEnabled()) {
            log.debug("当前使用从库: {}", slaveKey);
        }

        return slaveKey;
    }
}
