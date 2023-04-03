package com.linjf.cloud.config;

import feign.Feign;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author linjf
 * @create 2023/3/17 20:03
 */
@ConditionalOnClass(Feign.class)
@Configuration
public class OpenFeignConfig {
    /**
     * 注册一个重试 Bean
     * OpenFeign 发起重试需要抛出 RetryableException
     * 默认FeignClient不会进行重试，使用的是{@link feign.Retryer#NEVER_RETRY}
     * 参考:https://blog.csdn.net/Chenhui98/article/details/126314987
     */
    @Bean
    public Retryer feignRetryer() {
        /** 重试间隔100ms，最大重试间隔时间为1秒，重试次数为3次 */
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1L), 3);
    }

    /** 自定义异常解码器
     * @return OpenFeignErrorDecoder
     */
    @Bean
    public ErrorDecoder errorDecoder(){
        return new FeignErrorDecoder();
    }
}
