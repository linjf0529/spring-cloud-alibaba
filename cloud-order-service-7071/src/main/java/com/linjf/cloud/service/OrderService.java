package com.linjf.cloud.service;

import com.linjf.cloud.entity.Order;

/**
 * @author linjf
 * @create 2023/3/3 0:07
 * @company 厦门熙重电子科技有限公司
 */
public interface OrderService {
    /**
     * 查询订单信息
     * @param orderId
     * @return
     */
    Order queryOrderById(Long orderId);
}
