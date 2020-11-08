package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jd.health.constant.MessageConstant;
import com.jd.health.dao.MemberDao;
import com.jd.health.dao.OrderDao;
import com.jd.health.dao.OrderSettingDao;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.ClearOrder;
import com.jd.health.pojo.Member;
import com.jd.health.pojo.Order;
import com.jd.health.pojo.OrderSetting;
import com.jd.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    //提交预约
    @Override
    @Transactional
    public Order submitOrder(Map<String, String> orderInfo) {
        //定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        //转换前端日期格式
        Date orderDate=null;
        try {
            orderDate = sdf.parse(orderInfo.get("orderDate"));
        } catch (ParseException e) {
            throw new HealthException("日期格式不正确,请选择正确日期");
        }
        //根据预约日期查询是否该预约日期进行了设置
        OrderSetting orderSetting=orderSettingDao.findOrderSettingByOrderDate(orderDate);
        if (null == orderSetting) {
            throw new HealthException("所选日期暂不能预约,请重新选择日期");
        }
        //如果进行了设置,判断已预约人数和限制人数
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            throw new HealthException("当日预约人数已满,请重新选择日期");
        }
        //判断系统中是否有该用户
        //获取前端的手机号
        String telephone = orderInfo.get("telephone");
        //根据手机号查询会员信息
        Member member=memberDao.findMemberByTelephone(telephone);
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        //判断会员是否存在
        if (null != member) {
            //查询该会员是否重复预约
            order.setMemberId(member.getId());
            //根据用户id和时间和套餐查询是否有该预约记录
            List<Order> orderList = orderDao.findByCondition(order);
            if (null != orderList && orderList.size() > 0) {
                //说明已经有预约过
                throw new HealthException("该套餐已经预约过了，请不要重复预约");
            }

        } else {
            //会员不存在,则注册会员
            member = new Member();
            member.setName(orderInfo.get("name"));
            member.setSex(orderInfo.get("sex"));
            member.setIdCard(orderInfo.get("idCard"));
            member.setPhoneNumber(telephone);
            member.setPassword("123456");
            member.setRemark("由预约界面转入注册");
            member.setRegTime(new Date());
            //添加会员信息
            memberDao.add(member);
            order.setMemberId(member.getId());
        }
        //走到这里表明可以预约
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //添加预约信息
        orderDao.add(order);
        //更新预约人数
        int update=orderSettingDao.updateReservationByOrderDate(orderSetting);
        if (update<1) {
            throw new HealthException(MessageConstant.ORDER_FULL);
        }
        return order;
    }

    //根据预约id查询预约信息
    @Override
    public Map<String, Object> findOrderDetailById(int orderId) {
        return orderDao.findOrderDetailById(orderId);
    }

    //根据时间查询已预约信息
    @Override
    public List<ClearOrder> findOrderBeforeDate(String today) {
        return orderDao.findOrderBeforeDate(today);
    }

    //删除预约信息
    @Override
    public void deleteByDate(String today) {
        orderDao.deleteByDate(today);
    }

}
