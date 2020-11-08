package com.jd.health.dao;

import com.jd.health.pojo.ClearOrder;
import com.jd.health.pojo.Member;
import com.jd.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

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

    //今日预约数）
    int findOrderCountByDate(String reportDate);

    //（今日到诊数）
    int findVisitCountByDate(String reportDate);

    //区间预约数
    int findTotalCountBetween(@Param("startDate") String startDate,@Param("endDate") String endDate);

    //指定日期后到诊数）
    int findVisitCountAfterDate(String date);

    //查询热门套餐
    List<Map<String, Object>> findHotSetmeal();

    //根据日期查询已经预约的信息
    List<ClearOrder> findOrderBeforeDate(String today);

    //删除预约信息
    void deleteByDate(String today);
}
