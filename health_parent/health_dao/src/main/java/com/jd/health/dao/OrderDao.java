package com.jd.health.dao;

import com.jd.health.pojo.Member;
import com.jd.health.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
public interface OrderDao {
    //多条件查询是否有预约信息
    List<Order> findByCondition(Order order);
    //添加预约信息
    void add(Order order);

    //根据预约订单id查询预约信息
    Map<String, Object> findOrderDetailById(int orderId);
}
