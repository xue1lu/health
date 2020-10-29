package com.jd.health.service;

import com.jd.health.pojo.Order;

import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
public interface OrderService {
    //提交预约
    Order submitOrder(Map<String, String> orderInfo);

    //根据预约id查询预约信息
    Map<String, Object> findOrderDetailById(int orderId);
}
