package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.constant.RedisMessageConstant;
import com.jd.health.entity.Result;
import com.jd.health.pojo.Member;
import com.jd.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    @PostMapping("/check")
    public Result check(@RequestBody Map<String, String> loginInfo, HttpServletResponse res) {

        //获得telephone
        String telephone = loginInfo.get("telephone");
        //获得验证码
        String validateCode = loginInfo.get("validateCode");
        //获得jedis
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_LOGIN+ "_" + telephone;
        String codeInRedis = jedis.get(key);
        if (StringUtils.isEmpty(codeInRedis)) {
            //没有验证码,重新发送
            return new Result(false, MessageConstant.RSEND_VALIDATECODE);
        }
        if (!codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //走到这里说明验证码可用,删除redis缓存
        jedis.del(key);
        //判断是否为会员
        Member member=memberService.findMemberByTelephone(telephone);
        if (null == member) {
            //如果会员不存在,注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setRemark("手机号快速注册");
            //注册
            memberService.add(member);
        }
        //记录手机号
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);
        //响应
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);

    }
}
