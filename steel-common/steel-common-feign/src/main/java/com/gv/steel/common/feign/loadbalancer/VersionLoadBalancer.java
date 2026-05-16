package com.gv.steel.common.feign.loadbalancer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gv.steel.common.feign.loadbalancer.chooser.IRuleChooser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class VersionLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final String VERSION_HEADER = "version";
    /**
     * 服务发现提供的服务实例列表
     */
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers;
    /**
     * 负责均衡 策略
     */
    private IRuleChooser ruleChooser;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        RequestData requestData = ((RequestDataContext) request.getContext()).getClientRequest();
        String version = getVersionFromRequestHeader(requestData);
        log.info("选择的版本号为：{}", version);
        return Objects.requireNonNull(this.serviceInstanceListSuppliers.getIfAvailable())
                .get(request).next()
                .map((instanceList) -> this.getInstanceResponse(instanceList, version));
    }

    private String getVersionFromRequestHeader(RequestData requestData) {
        return requestData.getHeaders().containsKey(VERSION_HEADER) ? requestData.getHeaders().get(VERSION_HEADER).get(0) : null;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instanceList, String version) {
        if (StrUtil.isBlank(version)) {
            ServiceInstance instance = ruleChooser.choose(instanceList);
            log.info("选择的IP为: {}, 端口为: {}, 实例ID为: {}", instance.getHost(), instance.getPort(), instance.getServiceId());
            return new DefaultResponse(instance);
        }

        // 通过请求头的版本号和微服务实例中的meta data 中的version匹配
        List<ServiceInstance> versionFilterInstanceList = instanceList.stream().filter(instance -> instance.getMetadata().containsKey(VERSION_HEADER) && version.equals(instance.getMetadata().get(VERSION_HEADER))).collect(Collectors.toList());

        boolean versionLoadbalancer = true;

        // 未匹配，使用默认的实例列表进行负载均衡
        if (CollUtil.isEmpty(versionFilterInstanceList)) {
            versionFilterInstanceList = instanceList;
            versionLoadbalancer = false;
        }

        ServiceInstance instance = ruleChooser.choose(versionFilterInstanceList);
        log.info("选择的IP为: {}, 端口为: {}, 实例ID为: {}, version: {}", instance.getHost(), instance.getPort(), instance.getServiceId(), versionLoadbalancer ? version : version + "[未匹配到对应版本的服务实例]");
        return new DefaultResponse(instance);
    }
}
