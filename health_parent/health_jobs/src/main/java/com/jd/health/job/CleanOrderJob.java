package com.jd.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.pojo.ClearOrder;
import com.jd.health.service.OrderService;
import com.jd.health.service.OrderSettingService;
import com.jd.health.service.SetmealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
//清理过期预约数据
@Component
public class CleanOrderJob {
    @Reference
    private OrderService orderService;
    @Reference
    private OrderSettingService orderSettingService;
    //定义打印log日志对象
    private static final Logger log = LoggerFactory.getLogger(CleanOrderJob.class);
    //每天24点开始清理数据
    @Scheduled(cron="0 0 24 * * ? ")
    public void cleanOrder() {
        log.info("清理过期预约数据任务启动了");
        //定义日期格式
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        //设置日历为today-1
        String today = sdf.format(new Date());
        //查询指定日期之前的预约信息
        List<ClearOrder> orderList=orderService.findOrderBeforeDate(today);

        for (ClearOrder clearOrder : orderList) {
            //获取日期
            Date orderDate = clearOrder.getOrderDate();
            String date = sdf.format(orderDate);
            //获取数量
            int orderCount = clearOrder.getCount();
            //更新预约设置信息
            orderSettingService.updateReservationByDate(orderCount,date);
        }
        log.info("预约设置表更新完成了");
        //删除预约信息
        orderService.deleteByDate(today);
        log.info("预约信息表更新完成了");
    }
}
