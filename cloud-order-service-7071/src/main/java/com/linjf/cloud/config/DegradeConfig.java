package com.linjf.cloud.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linjf
 * @create 2023/3/17 7:02
 * 自定义降级规则
 */
@Configuration
public class DegradeConfig {
    @PostConstruct
    public void initDegradeRule(){
        List<DegradeRule> degradeRuleList=new ArrayList<>();
        //定义降级规则
        DegradeRule degradeRule=new DegradeRule();
        //设置降级规则所对应的资源名称
        degradeRule.setResource("degradeRule");
        //根据发生异常的次数进行降级
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        //至少要有两次请求才会降级，第一次请求时发生异常也不会降级
        degradeRule.setMinRequestAmount(2);
        //发生异常的次数达到1后开始降级
        degradeRule.setCount(1);
        //降级的时间窗口，在10秒内，所有的请求都直接降级
        degradeRule.setTimeWindow(10);
        //表示熔断后10秒内的其他调用都走熔断方法
        degradeRule.setStatIntervalMs(10000);
        //把降级规则添加到降级规则列表
        degradeRuleList.add(degradeRule);
        //把降级列表添加到降级规则管理器
        DegradeRuleManager.loadRules(degradeRuleList);
    }
}
