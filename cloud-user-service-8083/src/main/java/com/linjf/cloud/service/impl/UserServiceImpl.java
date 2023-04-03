package com.linjf.cloud.service.impl;

import com.linjf.cloud.dao.UserMapper;
import com.linjf.cloud.entity.User;
import com.linjf.cloud.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User findUserById(Long id) {
        return userMapper.findUserById(id);
    }
}
