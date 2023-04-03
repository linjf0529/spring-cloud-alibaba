package com.linjf.es.dao;

import com.linjf.es.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HotelMapper {
    /**
     * id查询
     * @param id
     * @return
     */
    Hotel findById(@Param("id")Long id);


    /**
     * 查询全部
     * @return
     */
    List<Hotel> findList();
}
