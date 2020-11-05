package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jd.health.dao.MemberDao;
import com.jd.health.pojo.Member;
import com.jd.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass =MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    //根据telephone查询会员信息
    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }

    //注册会员
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    //查询指定月份之前的累计会员数量
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        //定义数据模型
        List<Integer> memberCount = new ArrayList<Integer>();
        if (months != null && months.size() > 0) {
            //遍历月份数据
            for (String month : months) {
                //调用dao层
                Integer count=memberDao.findMemberCountByBeforeDate(month + "-31");
                memberCount.add(count);
            }
        }
        return memberCount;
    }
}
