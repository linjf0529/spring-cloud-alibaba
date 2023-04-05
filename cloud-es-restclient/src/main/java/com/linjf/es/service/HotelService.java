package com.linjf.es.service;

import com.linjf.es.entity.Hotel;

import java.util.List;

/**
 * @author linjf
 * @create 2023/3/15 5:05
 */
public interface HotelService {
    /**
     * id查询
     * @param id
     * @return
     */
    Hotel findById(Long id);


    /**
     * 查询全部
     * @return
     */
    List<Hotel> findList();
}
