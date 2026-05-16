package com.gv.steel.common.sharding.algorithm;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * 月份分表算法
 * table_name_yyyyMM
 */
public class MonthDateShardingAlgorithm implements PreciseShardingAlgorithm<LocalDateTime>, RangeShardingAlgorithm<LocalDateTime> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<LocalDateTime> shardingValue) {
        return builderShardingTable(shardingValue.getLogicTableName(), shardingValue.getValue());
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<LocalDateTime> shardingValue) {
        Range<LocalDateTime> dateRange = shardingValue.getValueRange();
        LocalDateTime startDate = dateRange.lowerEndpoint();
        LocalDateTime endDate = dateRange.upperEndpoint();
        Collection<String> tableNames = Lists.newArrayList();
        while (!startDate.isAfter(endDate)) {
            tableNames.add(builderShardingTable(shardingValue.getLogicTableName(), startDate));
            startDate = startDate.plusMonths(1);
        }
        return tableNames;
    }

    private String builderShardingTable(String logicTableName, LocalDateTime date) {
        String yearMonth = date.format(DateTimeFormatter.ofPattern("yyyyMM"));
        return StrUtil.join("_", logicTableName, yearMonth);
    }
}
