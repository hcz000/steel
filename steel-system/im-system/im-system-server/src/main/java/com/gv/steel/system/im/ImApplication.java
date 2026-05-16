package com.gv.steel.system.im;

import com.gv.steel.common.security.annotation.EnableResourceServer;
import com.gv.steel.common.swagger.annotation.EnableDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDoc
@EnableResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients({"com.gv.steel"})
public class ImApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImApplication.class, args);
    }
}
