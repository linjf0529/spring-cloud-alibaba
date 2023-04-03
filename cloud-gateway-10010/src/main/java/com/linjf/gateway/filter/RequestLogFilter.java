package com.linjf.gateway.filter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.linjf.gateway.properties.CustomGatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;

/**
 * @author linjf
 * @create 2023/3/6 16:45
 * gateway 日志
 */
@Slf4j
@Component
public class RequestLogFilter implements GlobalFilter, Ordered {

    @Autowired
    private CustomGatewayProperties customGatewayProperties;

    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 读取 nacos 是否开启日志
        if (!customGatewayProperties.getRequestLog()) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        String path = getOriginalRequestUrl(exchange);
        String url = request.getMethod().name() + " " + path;

        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        boolean flag = StringUtils.startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
        // 打印请求参数
        if (flag) {
            // 从缓存中读取body
            Object obj = exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
            String jsonParam = null;
            if (ObjectUtil.isNotNull(obj)) {
                DataBuffer buffer = (DataBuffer) obj;
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
                jsonParam = charBuffer.toString();
            }
            log.debug("[PLUS]开始请求 => URL[{}],参数类型[json],参数:[{}]", url, jsonParam);
        } else {
            MultiValueMap<String, String> parameterMap = request.getQueryParams();
            if (MapUtil.isNotEmpty(parameterMap)) {
                String parameters = JSON.toJSONString(parameterMap);
                log.debug("[PLUS]开始请求 => URL[{}],参数类型[param],参数:[{}]", url, parameters);
            } else {
                log.debug("[PLUS]开始请求 => URL[{}],无参数", url);
            }
        }

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                long executeTime = (System.currentTimeMillis() - startTime);
                log.debug("[PLUS]结束请求 => URL[{}],耗时:[{}]毫秒", url, executeTime);
            }
        }));
    }

    @Override
    public int getOrder() {
        // 一定要放在最后
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * 获取原始请求路径
     */
    public static String getOriginalRequestUrl(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        LinkedHashSet<URI> uris = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI requestUri = uris.stream().findFirst().orElse(request.getURI());
        return UriComponentsBuilder.fromPath(requestUri.getRawPath()).build().toUriString();
    }

}

