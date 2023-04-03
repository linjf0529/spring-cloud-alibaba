package com.linjf.cloud.service.impl;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.linjf.cloud.client.UserClient;
import com.linjf.cloud.dao.OrderMapper;
import com.linjf.cloud.entity.Order;
import com.linjf.cloud.entity.User;
import com.linjf.cloud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserClient userClient;

    /**
     * 默认情况下，service中的方法是不被Sentinel监控的，需要通过注解(@SentinelResource)来标记要监控的方法。
     * 链路模式中，是对不同来源的两个链路做监控。但是sentinel默认会给进入SpringMVC的所有请求设置同一个root资源，会导致链路模式失效。
     * 需要关闭这种对SpringMVC的资源聚合，修改order-service服务的application.yml文件：
     * spring.cloud.sentinel.web-context-unify: false 关闭context整合
     * @param orderId
     * @return
     */
    @SentinelResource("service-order")
    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.用Feign远程调用
        User user = userClient.queryById(order.getUserId());
        // 3.封装user到Order
        order.setUser(user);
        // 4.返回
        return order;
    }

}
