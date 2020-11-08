package com.jd.health.dao;

import com.jd.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
public interface OrderSettingDao {
    //根据日期更新可预约的人数
    OrderSetting findNumberByOrderDate(Date orderDate);

    //更新可预约的信息
    void updateNumberByOrderDate(OrderSetting orderSetting);

    //添加预约设置信息
    void add(OrderSetting orderSetting);

    //根据月份查询预约信息
    List<OrderSetting> findOrderSettingByMonth(String month);


    //根据日期查询预约设置
    OrderSetting findOrderSettingByOrderDate(Date orderDate);

    //更新预约人数
    int updateReservationByOrderDate(OrderSetting orderSetting);

    //根据日期更新预约设置信息
    void updateReservationByDate(@Param("count") int orderCount,@Param("date") String date);
}
