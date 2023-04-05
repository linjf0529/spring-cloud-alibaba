package com.linjf.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author linjf
 * @create 2023/3/6 16:58
 
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "spring.cloud.gateway")
public class CustomGatewayProperties {

    /**
     * 请求日志
     */
    private Boolean requestLog=true;

}
