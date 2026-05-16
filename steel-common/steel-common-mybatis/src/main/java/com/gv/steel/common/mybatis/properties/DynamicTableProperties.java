package com.gv.steel.common.mybatis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;
import java.util.Map;

/**
 *
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DynamicTableProperties.PREFIX)
public class DynamicTableProperties {
    public final static String PREFIX = "steel.dynamic-table";

    /**
     * 是否开启动态表
     */
    private Boolean enable = false;

    /**
     * 月份动态表配配置(tableName: properties)
     */
    private Map<String, MonthDateDynamicTableProperties> monthDateDynamicTable;

    /**
     * id hash 动态表配置(tableName: properties)
     */
    private Map<String, IdHashDynamicTableProperties> idHashDynamicTable;


    @Getter
    @Setter
    public static class MonthDateDynamicTableProperties {
        /**
         * 月份参数名
         * 通过该参数名从上下文中查询的值
         * 值必须为 {@link LocalDate}
         */
        private String monthDateParam;
    }

    @Getter
    @Setter
    public static class IdHashDynamicTableProperties {
        /**
         * id取值参数名
         * 通过该参数名从上下文中查询的值
         * 值必须为 {@link Long}
         */
        private String idDateParam;

        /**
         * hash 因子（分表数）
         */
        private Integer hashFactor;
    }
}
