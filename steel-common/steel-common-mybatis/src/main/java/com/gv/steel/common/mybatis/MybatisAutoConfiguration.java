package com.gv.steel.common.mybatis;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import com.gv.steel.common.core.factory.YamlPropertySourceFactory;
import com.gv.steel.common.mybatis.aop.DynamicTableParamAop;
import com.gv.steel.common.mybatis.handler.MybatisDataPermissionHandler;
import com.gv.steel.common.mybatis.handler.MybatisDynamicTableNameHandler;
import com.gv.steel.common.mybatis.handler.MybatisPlusMetaObjectHandler;
import com.gv.steel.common.mybatis.handler.MybatisTenantLineHandler;
import com.gv.steel.common.mybatis.injector.CustomSqlInjector;
import com.gv.steel.common.mybatis.plugins.CustomPaginationInnerInterceptor;
import com.gv.steel.common.mybatis.properties.DynamicTableProperties;
import com.gv.steel.common.mybatis.properties.TenantLineProperties;
import com.gv.steel.common.mybatis.resolver.SqlFilterArgumentResolver;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mybatis plus 自动装配
 */
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({TenantLineProperties.class, DynamicTableProperties.class})
@PropertySource(factory = YamlPropertySourceFactory.class, value = {"classpath:mybatis.yml"})
public class MybatisAutoConfiguration implements WebMvcConfigurer {

    /**
     * 分页最大限制数
     */
    private static final Long MAX_LIMIT = 1000L;
    private final TenantLineProperties tenantLineProperties;

    private final DynamicTableProperties dynamicTableProperties;

    /**
     * SQL 过滤器避免SQL 注入
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SqlFilterArgumentResolver());
    }

    /**
     * mybatis-plus 拦截器管理
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 动态表名
        if (dynamicTableProperties.getEnable()
                && (ArrayUtil.isNotEmpty(dynamicTableProperties.getIdHashDynamicTable())
                || ArrayUtil.isNotEmpty(dynamicTableProperties.getMonthDateDynamicTable()))
        ) {
            interceptor.addInnerInterceptor(customerDynamicTableNameInnerInterceptor());
        }
        // 多租户模式
        if (tenantLineProperties.getEnable()) {
            interceptor.addInnerInterceptor(tenantLineInnerInterceptor());
        }
        // 自定义数据权限
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(new MybatisDataPermissionHandler()));
        // 分页处理
        interceptor.addInnerInterceptor(customPaginationInnerInterceptor());
        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防止全表更新与删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    private DynamicTableNameInnerInterceptor customerDynamicTableNameInnerInterceptor() {
        return new DynamicTableNameInnerInterceptor(new MybatisDynamicTableNameHandler(dynamicTableProperties));
    }

    private TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new MybatisTenantLineHandler(tenantLineProperties));
    }

    private CustomPaginationInnerInterceptor customPaginationInnerInterceptor() {
        CustomPaginationInnerInterceptor paginationInnerInterceptor = new CustomPaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(MAX_LIMIT);
        paginationInnerInterceptor.setOverflow(false);
        return paginationInnerInterceptor;
    }

    /**
     * 审计字段自动填充
     *
     * @return {@link MetaObjectHandler}
     */
    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

    @Bean
    public DefaultSqlInjector customSqlInjector() {
        return new CustomSqlInjector();
    }

    @Bean
    @ConditionalOnProperty(value = "steel.dynamic-table.enable", havingValue = "true")
    public DynamicTableParamAop dynamicTableParamAop() {
        return new DynamicTableParamAop(dynamicTableProperties);
    }
}
