package com.jd.health.service;

import com.jd.health.pojo.Member;

/**
 * @Auther lxy
 * @Date
 */
public interface MemberService {
    //根据telephone查询会员信息
    Member findMemberByTelephone(String telephone);

    //注册会员
    void add(Member member);
}
