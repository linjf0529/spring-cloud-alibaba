package com.linjf.cloud.controller;


import com.linjf.cloud.entity.User;
import com.linjf.cloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 测试路径:测试地址:http://localhost:8083/user/1
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") Long id) {
        User user=userService.findUserById(id);
        user.setServicePort(port);
        return user;
    }
}
