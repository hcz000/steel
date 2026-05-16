package com.gv.steel.common.mybatis.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.gv.steel.common.mybatis.properties.TenantLineProperties;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;

@AllArgsConstructor
public class MybatisTenantLineHandler implements TenantLineHandler {
    private TenantLineProperties tenantLineProperties;

    @Override
    public Expression getTenantId() {
        // TODO tenant isolate
//        return (Expression)(StrUtil.isNotBlank(tenantId) ? new StringValue(tenantId) : new NullValue());
        return new NullValue();
    }

    @Override
    public String getTenantIdColumn() {
        return tenantLineProperties.getTenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return tenantLineProperties.getIgnoreTables().contains(tableName);
    }
}
