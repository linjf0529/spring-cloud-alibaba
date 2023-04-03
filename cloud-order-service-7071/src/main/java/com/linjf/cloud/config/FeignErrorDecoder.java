package com.linjf.cloud.config;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author linjf
 * @create 2023/3/17 19:55
 * 扩展ErrorDecoder
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();
    /**
     *
     * @param methodKey 方法名
     * @param response 响应体
     * @return
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("feign调用异常::>>{} client error,response is {}:",methodKey,response);
        Exception exception = defaultErrorDecoder.decode(methodKey, response);
        if(exception instanceof RetryableException){
            return exception;
        }
        /**
         * 查看:SynchronousMethodHandler类
         * 1.Feign默认配置是不走重试策略的，当发生RetryableException异常时直接抛出异常。
         * 2.并非所有的异常都会触发重试策略，只有 RetryableException 异常才会触发异常策略。
         * 3.在默认Feign配置情况下，只有在网络调用时发生 IOException 异常时，才会抛出 RetryableException，也是就是说链接超时、读超时等不不会触发此异常。
         * 因此常见的 SocketException、NoHttpResponseException、UnknownHostException、HttpRetryException、SocketConnectException、ConnectionClosedException 等异常都可触发Feign重试机制。
         */
        if(500==response.status()){
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    new Date(),
                    response.request() );
        }
        return exception;
    }
}
