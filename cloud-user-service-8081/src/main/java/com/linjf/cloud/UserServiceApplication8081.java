package com.linjf.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * nacos服务注册步骤:
 * 1.引入nacos相关依赖(spring-cloud-starter-alibaba-nacos-discovery)
 * 2.配置nacos服务地址(spring.cloud.nacos.server-addr)
 * 3.在主配置类添加@EnableDiscoveryClient注解
 */

@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceApplication8081 {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication8081.class, args);
    }
}
