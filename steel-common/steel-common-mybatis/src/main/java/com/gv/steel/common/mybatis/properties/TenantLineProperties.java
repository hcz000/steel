package com.gv.steel.common.mybatis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "steel.tenant")
public class TenantLineProperties {
    /**
     * 是否启用多租户模式
     */
    private Boolean enable = false;
    /**
     * 不需要租户模式表
     */
    private List<String> ignoreTables = new ArrayList<>();
    /**
     * 租户表对应字段
     */
    private String tenantIdColumn = "tenant_id";
    /**
     * 忽略的列
     */
    private List<String> ignoreColumn = new ArrayList<>();
}
