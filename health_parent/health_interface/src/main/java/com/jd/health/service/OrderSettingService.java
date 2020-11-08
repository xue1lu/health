package com.jd.health.service;

import com.jd.health.exception.HealthException;
import com.jd.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
public interface OrderSettingService {

    //上传预约设置信息
    void add(List<OrderSetting> orderSettingList)throws HealthException;

    //根据月份查询预约信息
    List<Map<String, Integer>> findOrderSettingByMonth(String month);

    //根据日期更新预约设置人数
    void updateNumberByOrderDate(OrderSetting orderSetting)throws HealthException;

    //根据日期更新预约设置信息
    void updateReservationByDate(int orderCount, String date);


}
