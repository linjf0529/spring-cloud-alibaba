package com.linjf.cloud.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Component
public class SpringRabbitListener {


    @RabbitListener(queues = "simple.queue")
    public void listenWorkQueue(Map<String,Object> msgMap){
        log.info("order::消费者接收到消息==>>{}",msgMap);
    }

    /**
     *基于广播
     */
    @RabbitListener(queues = "fanout.order")
    public void listenFanoutOrder(String msg) {
        log.info("消费者接收到fanout.order的消息==>>{}",msg);
    }
    @RabbitListener(queues = "fanout.user")
    public void listenFanoutUser(String msg) {
        log.info("消费者接收到fanout.user的消息==>>{}",msg);
    }

    /**
     * 基于路由
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.order"),
            exchange = @Exchange(name = "linjf.direct", type = ExchangeTypes.DIRECT),
            key = {"pay", "order"}
    ))
    public void listenDirectOrder(String msg){
        log.info("消费者接收到direct.order的消息==>>{}",msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.user"),
            exchange = @Exchange(name = "linjf.direct", type = ExchangeTypes.DIRECT),
            key = {"pay", "user"}
    ))
    public void listenDirectUser(String msg){
        log.info("消费者接收到direct.user的消息==>>{}",msg);
    }

    /**
     *基于话题
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.china"),
            exchange = @Exchange(name = "linjf.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenTopicChina(String msg){
        log.info("消费者topic.china接收到消息==>>{}",msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.news"),
            exchange = @Exchange(name = "linjf.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicNews(String msg){
        log.info("消费者topic.news接收到消息==>>{}",msg);
    }
}
