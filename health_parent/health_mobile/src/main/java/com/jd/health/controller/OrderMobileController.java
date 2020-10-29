package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.constant.RedisMessageConstant;
import com.jd.health.entity.Result;
import com.jd.health.pojo.Order;
import com.jd.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    //注入jedis
    @Autowired
    private JedisPool jedisPool;
    //订阅预约查询服务
    @Reference
    private OrderService orderService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> paraMap) {
        //获取jedis
        Jedis jedis = jedisPool.getResource();
        //获取手机号
        String telephone = paraMap.get("telephone");
        //验证码判断
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        String codeInRedis = jedis.get(key);
        if (StringUtils.isEmpty(codeInRedis)) {
            //没有验证码,重新发送
            return new Result(false, MessageConstant.RSEND_VALIDATECODE);
        }
        //如果有验证码,比较
        //获取前端传的验证码
        String validateCode = paraMap.get("validateCode");
        if (!codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //走到这里表示验证码通过,移除生成的验证码
       jedis.del(key);//测试时可不执行
        //设置预约类型
        paraMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        //调用业务层
        Order order=orderService.submitOrder(paraMap);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);

    }

    //根据预约成功的id查询预约信息
    @PostMapping("/findById")
    public Result findById(@RequestParam("id") int orderId) {
       Map<String, Object> orderInfo= orderService.findOrderDetailById(orderId);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, orderInfo);
    }
}
