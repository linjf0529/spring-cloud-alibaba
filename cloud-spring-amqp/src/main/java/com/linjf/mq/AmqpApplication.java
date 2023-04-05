package com.linjf.mq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author linjf
 * @create 2023/3/6 21:01
 */
@SpringBootApplication
public class AmqpApplication {
    public static void main(String[] args) {
        SpringApplication.run(AmqpApplication.class,args);
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
