package com.linjf.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    @RabbitListener(queuesToDeclare = @Queue("simple.queue"))
    public void testSendMessage2SimpleQueue() {
        String queueName = "simple.queue";
        Map<String,Object> msgMap=new HashMap<>();
        msgMap.put("name","用户名");
        msgMap.put("age",18);
        msgMap.put("msg","hello, spring amqp!");
        rabbitTemplate.convertAndSend(queueName, msgMap);
    }


    /**
     * 广播模式(FanoutExchange)
     * 注意order模块下的FanoutConfig 需要把queue绑定到exchange
     */
    @Test
    public void testSendFanoutExchange() {
        // 交换机名称
        String exchangeName = "linjf.fanout";
        // 消息
        String message = "hello, every one!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    /**
     * 路由模式(DirectExchange)
     */
    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "linjf.direct";
        // 消息
        String message = "hello, user!";
        // 发送消息
        //routingKey:pay,order,user
        rabbitTemplate.convertAndSend(exchangeName, "user", message);
    }

    /**
     * 话题模式(TopicExchange)
     */
    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "linjf.topic";
        // 消息
        String message = "中国天气预报:日期2023年3月6号,天气:晴";
        // 发送消息
        //routingKey:china.* , *.news
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
    }
}
