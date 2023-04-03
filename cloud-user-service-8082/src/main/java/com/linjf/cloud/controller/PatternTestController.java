package com.linjf.cloud.controller;

import com.linjf.cloud.config.PatternProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author linjf
 * @create 2023/3/4 0:20
 * @company 厦门熙重电子科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/pattern")
public class PatternTestController {
    @Resource
    private PatternProperties properties;

    /**
     * 测试路径:测试地址:http://localhost:8082/pattern/prop
     * 测试nacos做为配置中心,实现统一配置和动态配置
     * @return
     */
    @GetMapping("/prop")
    public PatternProperties properties(){
        return properties;
    }
}
