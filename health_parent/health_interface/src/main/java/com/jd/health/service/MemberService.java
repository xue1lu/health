package com.jd.health.service;

import com.jd.health.pojo.Member;

import java.util.List;
import java.util.Map;

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

    //查询会员数量信息
    List<Map<String, Object>> findMemberCount();

    //查询指定年龄段会员数量
    List<Map<String, Object>> findMemberCountByAge();
}
