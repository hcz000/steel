package com.gv.steel.common.sharding.handler;

import cn.hutool.core.convert.Convert;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        LocalDateTime localDateTime = Convert.toLocalDateTime(rs.getObject(columnName));
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toLocalDate();
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        LocalDateTime localDateTime = Convert.toLocalDateTime(rs.getObject(columnIndex));
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toLocalDate();
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        LocalDateTime localDateTime = Convert.toLocalDateTime(cs.getObject(columnIndex));
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toLocalDate();
    }
}
