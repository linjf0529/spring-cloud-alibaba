package com.linjf.cloud.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于广播模式,需要把Queue(消息队列)绑定到fanoutExchange(交换机)
 */
@Configuration
public class FanoutConfig {

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("linjf.fanout");
    }

    @Bean
    public Queue fanoutUser(){
        return new Queue("fanout.user");
    }

    // 绑定队列user到交换机
    @Bean
    public Binding fanoutBindingUser(Queue fanoutUser, FanoutExchange fanoutExchange){
        return BindingBuilder
                .bind(fanoutUser)
                .to(fanoutExchange);
    }

    @Bean
    public Queue fanoutOrder(){
        return new Queue("fanout.order");
    }

    // 绑定队列order到交换机
    @Bean
    public Binding fanoutBinding2(Queue fanoutOrder, FanoutExchange fanoutExchange){
        return BindingBuilder
                .bind(fanoutOrder)
                .to(fanoutExchange);
    }

    @Bean
    public Queue objectQueue(){
        return new Queue("object.queue");
    }
}
