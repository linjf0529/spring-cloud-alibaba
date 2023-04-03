package com.linjf.cloud.client;

import com.linjf.cloud.client.fallback.UserClientFallbackFactory;
import com.linjf.cloud.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author linjf
 * @create 2023/3/3 0:59
 * 使用FeignClient必须开启@EnableFeignClients
 * 功能:
 *      1.可插拔的注解支持，包括Feign注解和JSX-RS注解。
 *      2.支持可插拔的HTTP编码器和解码器。
 *      3.支持Hystrix和它的Fallback。fallback = 回退方法
 *      4.支持Ribbon的负载均衡。
 *      5.支持HTTP请求和响应的压缩。
 * 在没有注册中心的情况下，openFeign也可以通过url指定服务地址
 * @FeignClient(url = "http://localhost:8081")
 * @FeignClient(value = "cloud-user-service",configuration = UserFallbackImpl.class)
 */

/**
 * FeignClient整合Sentinel
 * 1.修改配置文件开启Feign的Sentinel功能：feign.sentinel.enabled=true
 * 2.编写失败降级逻辑:方式一：FallbackClass，无法对远程调用的异常做处理 方式二：FallbackFactory，可以对远程调用的异常做处理
 */
@Component
@FeignClient(value = "userservice",fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
    @GetMapping(value = "/user/{id}")
    User queryById(@PathVariable("id") Long id);
}
