package com.linjf.cloud.client.fallback;

import com.linjf.cloud.client.UserClient;
import com.linjf.cloud.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author linjf
 * @create 2023/3/17 6:25
 
 */
@Slf4j
@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public User queryById(Long id) {
                log.error("查询用户异常", cause);
                return new User();
            }
        };
    }
}
