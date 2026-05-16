package com.gv.steel.common.security.annotation;

import com.gv.steel.common.security.component.ResourceServerAutoConfiguration;
import com.gv.steel.common.security.component.ResourceServerConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.lang.annotation.*;

/**
 * <p>
 * 资源服务注解
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ResourceServerAutoConfiguration.class, ResourceServerConfiguration.class})
public @interface EnableResourceServer {

}
