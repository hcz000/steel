package com.gv.steel.common.mybatis.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gv.steel.common.mybatis.annotation.query.Query;
import com.gv.steel.common.mybatis.annotation.query.QueryOrder;
import com.gv.steel.common.mybatis.annotation.query.QuerySort;
import com.gv.steel.common.mybatis.annotation.query.SelectColumn;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class QueryWrapperUtil {

    @SneakyThrows
    public <T extends Model<T>, Q> QueryWrapper<T> queryWrapperHandler(Q q) {
        // 获取查询对象的类和字段
        Class<?> qClass = q.getClass();
        Field[] fields = qClass.getDeclaredFields();

        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        SelectColumn selectColumn = qClass.getAnnotation(SelectColumn.class);
        if (selectColumn != null && ArrayUtil.isNotEmpty(selectColumn.value())) {
            // 转换字段名为下划线风格并设置为选择字段
            String[] strings = selectColumn.value();
            for (int i = 0; i < strings.length; i++) {
                strings[i] = StrUtil.toUnderlineCase(strings[i]);
            }
            queryWrapper.select(strings);
        }

        // 处理排序字段
        String sortColumn = "";
        String sortOrder = "";

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(q);

            // 处理@QuerySort注解
            QuerySort querySort = field.getDeclaredAnnotation(QuerySort.class);
            if (querySort != null) {
                String paramValue = Objects.toString(field.get(q), "");
                sortColumn = StrUtil.isEmpty(paramValue) ? querySort.value() : paramValue;
            }

            // 处理@QueryOrder注解
            QueryOrder queryOrder = field.getDeclaredAnnotation(QueryOrder.class);
            if (queryOrder != null) {
                String paramValue = Objects.toString(field.get(q), "");
                sortOrder = StrUtil.isEmpty(paramValue) ? queryOrder.value() : paramValue;
            }

            // 判断属性是否存在值
            if (ObjectUtil.isNull(value) || "null".equals(String.valueOf(value)) || "".equals(value)) {
                continue;
            }

            // 处理@Query注解
            Query query = field.getDeclaredAnnotation(Query.class);
            if (query == null) {
                continue;
            }

            // 获取列名
            String columnName = StrUtil.isBlank(query.column()) ? StrUtil.toUnderlineCase(field.getName()) : StrUtil.toUnderlineCase(query.column());

            // 根据注解生成查询条件
            switch (query.expression()) {
                case EQ:
                    queryWrapper.eq(columnName, value);
                    break;
                case NE:
                    queryWrapper.ne(columnName, value);
                    break;
                case LIKE:
                    queryWrapper.like(columnName, value);
                    break;
                case GT:
                    queryWrapper.gt(columnName, value);
                    break;
                case GE:
                    queryWrapper.ge(columnName, value);
                    break;
                case LT:
                    queryWrapper.lt(columnName, value);
                    break;
                case LE:
                    queryWrapper.le(columnName, value);
                    break;
                case IN:
                    queryWrapper.in(columnName, (List<?>) value);
                    break;
                case NOT_IN:
                    queryWrapper.notIn(columnName, (List<?>) value);
                    break;
                case IS_NULL:
                    queryWrapper.isNull(columnName);
                    break;
                case NOT_NULL:
                    queryWrapper.isNotNull(columnName);
                    break;
                case DATE_BETWEEN:
                    // 处理日期BETWEEN注解
                    if (value.getClass().isArray() && ((Object[]) value)[0] instanceof LocalDate) {
                        LocalDate[] arrayValue = (LocalDate[]) value;
                        queryWrapper.ge(columnName, arrayValue[0]).lt(columnName, arrayValue[1].plusDays(1));
                    } else {
                        throw new RuntimeException("Use arrays for range queries!");
                    }
                    break;
                case BETWEEN:
                    // 处理BETWEEN注解
                    if (value.getClass().isArray()) {
                        Object[] arrayValue = (Object[]) value;
                        queryWrapper.between(columnName, arrayValue[0], arrayValue[1]);
                    } else {
                        throw new RuntimeException("Use arrays for range queries!");
                    }
                    break;
            }
        }

        // 处理排序字段
        if ("desc".equalsIgnoreCase(sortOrder)) {
            queryWrapper.orderByDesc(StrUtil.isNotBlank(sortColumn), StrUtil.toUnderlineCase(sortColumn));
        } else {
            queryWrapper.orderByAsc(StrUtil.isNotBlank(sortColumn), StrUtil.toUnderlineCase(sortColumn));
        }

        return queryWrapper;
    }
}
