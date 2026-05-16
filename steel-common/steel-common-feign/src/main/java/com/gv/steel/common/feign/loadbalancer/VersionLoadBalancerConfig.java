package com.gv.steel.common.feign.loadbalancer;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.gv.steel.common.feign.loadbalancer.chooser.IRuleChooser;
import com.gv.steel.common.feign.loadbalancer.chooser.RoundRuleChooser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

@Slf4j
public class VersionLoadBalancerConfig {

    @Bean
    @ConditionalOnMissingBean({IRuleChooser.class})
    @ConditionalOnProperty(
            prefix = "custom.loadbalancer.isolation",
            value = {"chooser"}
    )
    public IRuleChooser customRuleChooser(Environment environment, ApplicationContext context) {
        IRuleChooser chooser = new RoundRuleChooser();
        if (environment.containsProperty("custom.loadbalancer.isolation.chooser")) {
            String chooserRuleClassString = environment.getProperty("custom.loadbalancer.isolation.chooser");
            if (StrUtil.isNotBlank(chooserRuleClassString)) {
                try {
                    Class<?> ruleClass = ClassUtils.forName(chooserRuleClassString, context.getClassLoader());
                    chooser = (IRuleChooser) ReflectUtil.newInstance(ruleClass, new Object[0]);
                } catch (ClassNotFoundException var6) {
                    log.error("没有找到定义的选择器，将使用内置的选择器", var6);
                }
            }
        }

        return chooser;
    }


    @Bean
    @ConditionalOnMissingBean({IRuleChooser.class})
    public IRuleChooser defaultRuleChooser() {
        return new RoundRuleChooser();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "custom.loadbalancer.isolation",
            name = {"enabled"},
            havingValue = "true",
            matchIfMissing = true
    )
    public ReactorServiceInstanceLoadBalancer versionServiceLoadBalancer(Environment environment, LoadBalancerClientFactory loadBalancerClientFactory, IRuleChooser ruleChooser) {
        String name = environment.getProperty("loadbalancer.client.name");
        return new VersionLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), ruleChooser);
    }
}
