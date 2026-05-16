package com.gv.steel.common.feign.loadbalancer.chooser;

import cn.hutool.core.collection.CollUtil;
import com.gv.steel.common.core.constant.ErrorCodeConstants;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询策略
 */
@Slf4j
public class RoundRuleChooser implements IRuleChooser {
    private final AtomicInteger position = new AtomicInteger(1000);

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (CollUtil.isNotEmpty(instances)) {
            return instances.get(Math.abs(this.position.incrementAndGet() % instances.size()));
        } else {
            throw new BaseException(MsgUtils.getSystemMessage(ErrorCodeConstants.NO_INSTANCE_AVAILABLE));
        }
    }
}
