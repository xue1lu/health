package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jd.health.constant.MessageConstant;
import com.jd.health.dao.OrderSettingDao;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.OrderSetting;
import com.jd.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    //上传预约设置信息
    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList)throws HealthException {

        //遍历日期和预约时间
            for (OrderSetting orderSetting : orderSettingList) {
                //查询预约日期对应的预约信息
                OrderSetting osInDb = orderSettingDao.findNumberByOrderDate(orderSetting.getOrderDate());
                if (osInDb != null) {
                    if (osInDb.getReservations() > orderSetting.getNumber()) {
                        //如果数据库中已经预约的人数大于要导入的预约设置人数
                        throw new HealthException(MessageConstant.IMPORT_ORDERSETTING_FAIL);
                    }
                    //反之根据日期更新可预约的人数
                    orderSettingDao.updateNumberByOrderDate(orderSetting);
                } else {
                    //预约信息不存在,可直接添加
                    orderSettingDao.add(orderSetting);
                }
            }

    }

    //根据月份查询预约信息
    @Override
    public List<Map<String, Integer>> findOrderSettingByMonth(String m_onth) {

        //日期使用模糊匹配
        String month = m_onth + "-%";
        //查询数据库
        List<OrderSetting> orderSettingList = orderSettingDao.findOrderSettingByMonth(month);
        //转换到list<map>中
        List<Map<String, Integer>>  mapList= new ArrayList<Map<String, Integer>>();
        if (orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                Map<String, Integer> map= new HashMap<String, Integer>();
                map.put("date", orderSetting.getOrderDate().getDate());
                map.put("number", orderSetting.getNumber());
                map.put("reservations", orderSetting.getReservations());
                mapList.add(map);
            }
        }
        return mapList;
    }

    //根据日期更新预约设置人数
    @Override
    public void updateNumberByOrderDate(OrderSetting orderSetting)throws HealthException {

        //首先根据日期查询预约上限
        OrderSetting osoInDb = orderSettingDao.findNumberByOrderDate(orderSetting.getOrderDate());
        //如果不为空
        if (osoInDb != null) {
            //判断预约人数是否已经超过要设置的上限
            if (osoInDb.getReservations() > orderSetting.getNumber()) {
                throw new HealthException("最大可预约人数不能小于已预约人数");
            }
            //反之更新最大可预约人数
            orderSettingDao.updateNumberByOrderDate(orderSetting);
        } else {
            //如果没有该设置则直接设置添加
            orderSettingDao.add(orderSetting);
        }


    }

    //根据日期更新预约设置信息
    @Override
    public void updateReservationByDate(int orderCount, String date) {
        orderSettingDao.updateReservationByDate(orderCount, date);
    }


}
