package com.linjf.cloud.controller;


import com.linjf.cloud.entity.User;
import com.linjf.cloud.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @Value("${server.port}")
    private String port;


    /**
     * 测试路径:测试地址:http://localhost:8081/user/1
     * @param id 用户id
     * @return 用户
     */
    @SneakyThrows
    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") Long id) {
        if(id==2){
            log.info("测试熔断抛出异常!");
            throw new RuntimeException("测试熔断!抛出异常");
        }
        if (id==3){
            log.info("线程休眠开始!!");
            Thread.sleep(1000);
            log.info("线程休眠结束!!");
        }
        User user=userService.findUserById(id);
        user.setServicePort(port);
        return user;
    }

    /**
     * 测试路径:测试地址:http://localhost:8081/add/header/user/1
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/add/header/{id}")
    public User gateWayAddHeaderTest(@PathVariable("id") Long id, @RequestHeader("X-Request-Foo") String header) {
        log.info("user-service-header====>{}",header);
        User user=userService.findUserById(id);
        user.setServicePort(port);
        return user;
    }
}
