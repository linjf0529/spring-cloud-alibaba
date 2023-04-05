package com.linjf.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author linjf
 * @create 2023/3/6 17:38
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取请求参数
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();
        // 2.获取authorization参数
        String auth = params.getFirst("authorization");
        //3.校验
        if("admin".equals(auth)){
            //4.放行
            return chain.filter(exchange);
        }
        //5.拦截 设置状态码
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    /**
     * 拦截器执行顺序,数字越小执行顺序越靠前.
     * 可以使用@Order注解替代
     * @return
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
