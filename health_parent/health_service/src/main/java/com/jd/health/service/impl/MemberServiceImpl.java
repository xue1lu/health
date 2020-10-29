package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jd.health.dao.MemberDao;
import com.jd.health.pojo.Member;
import com.jd.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

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
}
