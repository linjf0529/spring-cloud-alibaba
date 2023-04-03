package com.linjf.cloud.dao;


import com.linjf.cloud.entity.Order;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderMapper {

    Order findById(Long id);
}
