package com.gv.steel.system.base;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.security.annotation.EnableResourceServer;
import com.gv.steel.common.swagger.annotation.EnableDoc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDoc
@EnableResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients({"com.gv.steel"})
@MapperScan({CommonConstants.DEFAULT_DAO_PACKAGE})
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }
}
