package com.gv.steel.common.mybatis.handler.dynamic;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.gv.steel.common.mybatis.context.DynamicTableParamHolder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@RequiredArgsConstructor
public class MonthTableNameParser implements TableNameHandler {
    private final String dynamicParamName;

    @Override
    public String dynamicTableName(String sql, String tableName) {
        LocalDate monthDate = DynamicTableParamHolder.get(dynamicParamName);
        if (ObjUtil.isNull(monthDate)) {
            throw new IllegalArgumentException("please select month");
        }

        String suffix = monthDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
        return StrUtil.join("_", tableName, suffix);
    }

}
