package com.linjf.cloud;

import com.linjf.cloud.config.LoadBalancerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * nacos服务注册步骤:
 * 1.引入nacos相关依赖(spring-cloud-starter-alibaba-nacos-discovery)
 * 2.配置nacos服务地址(spring.cloud.nacos.server-addr)
 * 3.在主配置类添加@EnableDiscoveryClient注解
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients//启用feign客户端
@LoadBalancerClients(defaultConfiguration = LoadBalancerConfig.class)//配置负载均衡策略
public class OrderServiceApplication7071 {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication7071.class, args);
    }
}
