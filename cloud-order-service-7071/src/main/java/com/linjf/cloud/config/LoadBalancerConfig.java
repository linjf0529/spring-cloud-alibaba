package com.linjf.cloud.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancer;
import feign.Retryer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * @author linjf
 * @create 2023/3/3 20:56
 * 如果LoadBalancerConfig 上面加了@Configuration 注解，使用到的服务必须显式的在启动类上配置好
 * 1.直接使用 @LoadBalancerClients(defaultConfiguration = LoadBalancerConfig.class)，意思就是所以的服务都使用我们创建的LoadBalancerConfig中策略
 * 2.使用@LoadBalancerClients对不同的服务配置不同的策略
 *   @LoadBalancerClients(value = {
 *         @LoadBalancerClient(name = "stock-service", configuration = LoadBalancerConfig.class),
 *         @LoadBalancerClient(name = "stock-service1", configuration = LoadBalancerConfig1.class)
 *   })
 * 3.只有一个服务的时候直接使用@LoadBalancerClient配置
 *   @LoadBalancerClient(name = "stock-service", configuration = LoadBalancerConfig.class)
 * 没有加@Configuration 注解那么配了的服务会使用配置的负载均衡策略，没有配的服务会使用默认的策略
 *
 * 参考:https://blog.csdn.net/Zack_tzh/article/details/124445758
 */
@Configuration
public class LoadBalancerConfig {

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;


    /**
     * 默认 可以不声名
     *基于轮询(RoundRobinLoadBalancer)负载均衡策略
     * @param environment
     * @param loadBalancerClientFactory
     * @return
     */
    @Bean
    public ReactorLoadBalancer<ServiceInstance> roundRobinLoadBalancer(Environment environment,
                                                                       LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RoundRobinLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }

    /**
     *基于随机(RoundRobinLoadBalancer)负载均衡策略
     * @param environment
     * @param loadBalancerClientFactory
     * @return
     */
//    @Bean
//    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
//                                                                   LoadBalancerClientFactory loadBalancerClientFactory) {
//        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//        return new RandomLoadBalancer(
//                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
//    }

    /**
     * 基于集群权重(NacosLoadBalancer)负载均衡策略
     * spring-cloud-starter-alibaba-nacos-discovery提供
     * @param environment
     * @param loadBalancerClientFactory
     * @return
     */
//    @Bean
//    public ReactorLoadBalancer<ServiceInstance> nacosLoadBalancer(Environment environment,
//                                                                   LoadBalancerClientFactory loadBalancerClientFactory) {
//        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//        return new NacosLoadBalancer(
//                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
//                name,
//                nacosDiscoveryProperties);
//    }
}
