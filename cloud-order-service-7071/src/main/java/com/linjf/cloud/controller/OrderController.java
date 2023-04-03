package com.linjf.cloud.controller;


import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.nacos.common.model.RestResult;
import com.alibaba.nacos.common.model.RestResultUtils;
import com.linjf.cloud.base.ResponseResult;
import com.linjf.cloud.entity.Order;
import com.linjf.cloud.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * sentinel的流控模式:
 * 1.直接:统计当前资源的请求，触发阈值时对当前资源直接限流，也是默认的模式
 * 2.关联:统计与当前资源相关的另一个资源，触发阈值时，对当前资源限流
 * 3.链路：统计从指定链路访问到本资源的请求，触发阈值时，对指定链路限流
 *
 * sentinel的流控效果:
 * 1.快速失败:达到阈值后，新的请求会被立即拒绝并抛出FlowException异常。是默认的处理方式。
 * 2.warm up:预热模式，对超出阈值的请求同样是拒绝并抛出异常。但这种模式阈值会动态变化，从一个较小值逐渐增加到最大阈值。
 * 3.排队等待:让所有的请求按照先后次序排队执行，两个请求的间隔不能小于指定时长
 */
@RestController
@RequestMapping("/order")
public class OrderController {

   @Resource
   private OrderService orderService;

    /**
     * 测试服务熔断:http://localhost:7071/order/get/1
     * 正常请求:http://localhost:7071/order/get/101
     * 测试FeignClient调用失败(500)重试:http://localhost:7071/order/get/102
     * 测试FeignClient调用超时重试:http://localhost:7071/order/get/103
     *
     * degradeRule:DegradeConfig里面配置的降级规则 发生异常1次(不包含第一次请求)就开始降级
     * @param orderId
     * @return
     */
    @GetMapping("/get/{orderId}")
    @SentinelResource(value = "degradeRule",
            entryType = EntryType.OUT,
            blockHandler ="exceptionHandler",
            fallback = "oderFallback")
    public ResponseResult getOrderByUserId(@PathVariable("orderId") Long orderId) {
        if(orderId==1){
            throw new RuntimeException("测试熔断,抛出异常");
        }
        return ResponseResult.ok(orderService.queryOrderById(orderId));
    }

    /**
     * 熔断方法入参和入口方法保持一致,然后加上BlockException
     * @param orderId
     * @param ex
     * @return
     */
    public ResponseResult exceptionHandler(Long orderId,BlockException ex){
        ex.printStackTrace();
        return ResponseResult.fail("Oops,error occurred at,服务熔断::>oderId="+orderId);
    }

    /**
     * 兜底方法需要跟入口方法的参数保持一致
     * @param orderId
     * @return
     */
    public ResponseResult oderFallback(Long orderId) {
        return ResponseResult.fail("fallback error调用异常了::>orderId="+orderId);
    }

    @GetMapping("/query/{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
        return orderService.queryOrderById(orderId);
    }
}
