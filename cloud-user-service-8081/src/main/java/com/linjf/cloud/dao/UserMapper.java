package com.linjf.cloud.dao;



import com.linjf.cloud.entity.Payment;
import com.linjf.cloud.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    /**
     * @param id
     * @return
     */
    User findUserById(@Param("id") Long id);
}
