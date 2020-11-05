package com.jd.health.service;

import com.jd.health.pojo.Member;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface MemberService {
    //根据telephone查询会员信息
    Member findMemberByTelephone(String telephone);

    //注册会员
    void add(Member member);

    //查询指定月份之前的累计会员数量
    List<Integer> getMemberReport(List<String> months);
}
