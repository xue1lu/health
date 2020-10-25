package com.jd.health.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.Result;
import com.jd.health.pojo.OrderSetting;
import com.jd.health.service.OrderSettingService;
import com.jd.health.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    private static final Logger log = LoggerFactory.getLogger(OrderSettingService.class);

    @Reference
    private OrderSettingService orderSettingService;
    //上传预约设置信息
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            //读取excel文件内容
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //转成oedersetting实体类类型
            List<OrderSetting> orderSettingList = new ArrayList<OrderSetting>();
            //创建日期格式解析对象
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            //创建ordersetting对象存储数据
            OrderSetting os = null;
            //创建date对象接收解析后数据
            Date orderDate = null;
            //遍历读取的文件
            for (String[] dataArr : strings) {
                //读取日期
                orderDate = sdf.parse(dataArr[0]);
                //读取数量
                Integer value = Integer.valueOf(dataArr[1]);
                //对象填充值
                os = new OrderSetting(orderDate, value);
                //添加到集合中
                orderSettingList.add(os);
            }
            //调用服务添加预约设置信息
            orderSettingService.add(orderSettingList);
            //响应
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            log.error("上传预约设置失败",e);
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }

    //根据月份查询预约设置信息
    @GetMapping("/findOrderSettingByMonth")
    public Result findOrderSettingByMonth(String month) {
        //返回json数据格式为数组里面包含集合
        List<Map<String,Integer>> mapList=orderSettingService.findOrderSettingByMonth(month);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, mapList);
    }

    //根据日期更新预约设置人数
    @PostMapping("/updateNumberByOrderDate")
    public Result updateNumberByOrderDate(@RequestBody OrderSetting orderSetting) {
        orderSettingService.updateNumberByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
