package com.linjf.es.service.impl;

import com.linjf.es.dao.HotelMapper;
import com.linjf.es.entity.Hotel;
import com.linjf.es.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author linjf
 * @create 2023/3/15 5:06
 */
@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {
    private final HotelMapper hotelMapper;

    /**
     * id查询
     *
     * @param id
     * @return
     */
    @Override
    public Hotel findById(Long id) {
        return hotelMapper.findById(id);
    }

    /**
     * 查询全部
     * @return
     */
    @Override
    public List<Hotel> findList() {
        return hotelMapper.findList();
    }
}
