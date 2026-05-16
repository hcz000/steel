package com.gv.steel.common.mybatis.handler;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.gv.steel.common.mybatis.handler.dynamic.IdHashTableNameParser;
import com.gv.steel.common.mybatis.handler.dynamic.MonthTableNameParser;
import com.gv.steel.common.mybatis.properties.DynamicTableProperties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MybatisDynamicTableNameHandler implements TableNameHandler {
    private final DynamicTableProperties properties;

    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (CollUtil.isNotEmpty(properties.getMonthDateDynamicTable())
                && properties.getMonthDateDynamicTable().containsKey(tableName)) {
            DynamicTableProperties.MonthDateDynamicTableProperties monthProperties = properties.getMonthDateDynamicTable().get(tableName);
            return MonthTableNameParser.builder()
                    .dynamicParamName(monthProperties.getMonthDateParam())
                    .build()
                    .dynamicTableName(sql, tableName);
        }

        if (CollUtil.isNotEmpty(properties.getIdHashDynamicTable())
                && properties.getIdHashDynamicTable().containsKey(tableName)) {
            DynamicTableProperties.IdHashDynamicTableProperties idHashProperties = properties.getIdHashDynamicTable().get(tableName);
            return IdHashTableNameParser.builder()
                    .dynamicParamName(idHashProperties.getIdDateParam())
                    .hashFactor(idHashProperties.getHashFactor())
                    .build()
                    .dynamicTableName(sql, tableName);
        }

        return tableName;
    }
}
