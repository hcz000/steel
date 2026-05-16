package com.gv.steel.common.mybatis.handler.dynamic;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.gv.steel.common.mybatis.context.DynamicTableParamHolder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class IdHashTableNameParser implements TableNameHandler {
    private final String dynamicParamName;
    private final Integer hashFactor;

    @Override
    public String dynamicTableName(String sql, String tableName) {
        Long hashId = DynamicTableParamHolder.get(dynamicParamName);
        if (ObjUtil.isNull(hashId)) {
            throw new IllegalArgumentException("please input hash id");
        }

        long index = hashId % hashFactor;
        return StrUtil.join("_", tableName, index);
    }
}
