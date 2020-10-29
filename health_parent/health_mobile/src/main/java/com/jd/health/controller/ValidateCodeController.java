package com.jd.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.jd.health.constant.MessageConstant;
import com.jd.health.constant.RedisMessageConstant;
import com.jd.health.entity.Result;
import com.jd.health.utils.SMSUtils;
import com.jd.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    //注入jedis
    @Autowired
    private JedisPool jedisPool;
    private static final Logger log = LoggerFactory.getLogger(ValidateCodeController.class);

    @PostMapping("/send4Order")
    public Result send4Order(String telephone) throws Exception{
        //获得jedis
        Jedis jedis = jedisPool.getResource();
        //判断是否已经发送过验证码
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        String codeInRedis = jedis.get(key);
        if (codeInRedis == null) {
            //如果不存在则发送验证码
            //生成验证码
            try {
                Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
                //发送验证码
                //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode+"");
                log.debug("验证码发送成功 手机:{}, 验证码:{}", telephone, validateCode);
                //发完存入到redis中,并且设置有效时间
                jedis.setex(key, 15 * 60, validateCode+"");
                //响应
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                log.error(MessageConstant.SEND_VALIDATECODE_FAIL,e);
            }
        }
        //如果redis中已经有验证码
        return new Result(false, MessageConstant.SEND_VALIDATECODE_AREADY);
    }
    //手机号登录
    @PostMapping("/send4Login")
    public Result send4Login(String telephone) {
        //获得jedis
        Jedis jedis = jedisPool.getResource();
        //判断是否已经发送过验证码
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        String codeInRedis = jedis.get(key);
        if (codeInRedis == null) {
            //如果不存在则发送验证码
            //生成验证码
            try {
                Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
                //发送验证码
                //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode+"");
                log.debug("验证码发送成功 手机:{}, 验证码:{}", telephone, validateCode);
                //发完存入到redis中,并且设置有效时间
                jedis.setex(key, 15 * 60, validateCode+"");
                //响应
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                log.error(MessageConstant.SEND_VALIDATECODE_FAIL,e);
            }
        }
        //如果redis中已经有验证码
        return new Result(false, MessageConstant.SEND_VALIDATECODE_AREADY);
    }
}
