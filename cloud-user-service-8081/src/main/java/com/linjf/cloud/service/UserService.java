package com.linjf.cloud.service;

import com.linjf.cloud.entity.User;

/**
 * @author linjf
 * @create 2023/3/2 22:45
 * @company 厦门熙重电子科技有限公司
 */
public interface UserService {
    public User findUserById(Long id);
}
