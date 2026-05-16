package com.gv.steel.common.feign;

import com.gv.steel.common.feign.loadbalancer.VersionLoadBalancerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;


@ConditionalOnProperty(
        prefix = "custom.loadbalancer.isolation",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
@LoadBalancerClients(defaultConfiguration = {VersionLoadBalancerConfig.class})
public class VersionIsolationAutoConfig {
}
