package com.gv.steel.common.log.event;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.gv.steel.common.core.config.TaskExecutorConfiguration;
import com.gv.steel.common.core.jackson.Java8TimeModule;
import com.gv.steel.common.log.properties.LogProperties;
import com.gv.steel.system.user.feign.RemoteLogService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

/**
 * 异步监听日志事件
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener implements InitializingBean {

    // new 一个 避免日志脱敏策略影响全局ObjectMapper
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final RemoteLogService remoteLogService;

    private final LogProperties logProperties;

    @Order
    @SneakyThrows
    @EventListener(SysLogEvent.class)
    @Async(value = TaskExecutorConfiguration.EXECUTOR_BEAN_NAME)
    public void saveSysLog(SysLogEvent event) {
        SysLogEventSource sysLog = (SysLogEventSource) event.getSource();

        // json 格式刷参数放在异步中处理，提升性能
        if (Objects.nonNull(sysLog.getBody())) {
            String params = objectMapper.writeValueAsString(sysLog.getBody());
            sysLog.setParams(StrUtil.subPre(params, logProperties.getMaxLength()));
        }

        remoteLogService.saveLog(sysLog);
    }

    @Override
    public void afterPropertiesSet() {
        objectMapper.addMixIn(Object.class, PropertyFilterMixIn.class);
        String[] ignorableFieldNames = logProperties.getExcludeFields();

        FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
                SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
        objectMapper.setFilterProvider(filters);
        objectMapper.registerModule(new Java8TimeModule());
    }

    @JsonFilter("filter properties by name")
    static class PropertyFilterMixIn {

    }

}
