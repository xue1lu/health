package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jd.health.dao.MemberDao;
import com.jd.health.dao.OrderDao;
import com.jd.health.service.ReportService;
import com.jd.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;



    //运营统计数据
    @Override
    public Map<String, Object> getBusinessReportData() {

        //定义数据模型
        Map<String, Object> reportData= new HashMap<String, Object>();
        //定义日期格式
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        //（当前时间）--
        Date today = new Date();
        String reportDate = sdf.format(today);
        //本周第一天
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(today));
        //本周日
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
        //本月第一天
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDayOfThisMonth());
        //本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        //（今日新增会员数）
        int todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //（总会员数）
        int totalMember = memberDao.findTotalCountMember();
        //（本周新增会员数）
        int thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //（本月新增会员数）
        int thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);
        //（今日预约数）
        int todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //（今日到诊数）
        int todayVisitsNumber = orderDao.findVisitCountByDate(reportDate);
        //（本周预约数）
        int thisWeekOrderNumber = orderDao.findTotalCountBetween(monday, sunday);
        //（本周到诊数）
        int thisWeekVisitsNumber = orderDao.findVisitCountAfterDate(monday);
        //（本月预约数）
        int thisMonthOrderNumber = orderDao.findTotalCountBetween(firstDayOfThisMonth, lastDayOfThisMonth);
        //（本月到诊数）
        int thisMonthVisitsNumber = orderDao.findVisitCountAfterDate(firstDayOfThisMonth);
        //（热门套餐（hotSetmeal
        List<Map<String, Object>> hotSetmeal= orderDao.findHotSetmeal();

        reportData.put("reportDate", reportDate);
        reportData.put("todayNewMember", todayNewMember);
        reportData.put("totalMember", totalMember);
        reportData.put("thisWeekNewMember", thisWeekNewMember);
        reportData.put("thisMonthNewMember", thisMonthNewMember);
        reportData.put("todayOrderNumber", todayOrderNumber);
        reportData.put("todayVisitsNumber", todayVisitsNumber);
        reportData.put("thisWeekOrderNumber", thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber", thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        reportData.put("hotSetmeal", hotSetmeal);
        return reportData;

    }


}
