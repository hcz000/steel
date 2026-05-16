package com.gv.steel.common.feign.loadbalancer.chooser;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * load balance rule choose interface
 */
public interface IRuleChooser {
    ServiceInstance choose(List<ServiceInstance> instances);
}
