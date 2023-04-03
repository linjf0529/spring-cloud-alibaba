package com.linjf.cloud.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author linjf
 * @create 2023/3/3 20:25
 */
@Configuration
public class ApplicationContextConfig {
    /**
     * @LoadBalanced:开启负载均衡的功能
     * SpringCloud中,有两种调用服务时客户端的负载均衡策略,一个是Ribbon,一个是Feign
     * Ribbon:是一个基于Http端的负载均衡,通过在Configuration中配置RestTemplate来进行调用,可以自定义负载均衡的方式.
     * Feign:是一个通过本地接口的形式来进行调用服务的,其中Feign中默认引入了Ribbon.
     * @return
     */
    @Bean
    @LoadBalanced //负载均衡注解
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    /**
     * 替换默认的消息转换器
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
