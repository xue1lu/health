package com.jd.health.dao;

import com.jd.health.pojo.Member;

/**
 * @Auther lxy
 * @Date
 */
public interface MemberDao {
    //添加会员信息
    void add(Member member);

    //根据手机号查询会员信息
    Member findMemberByTelephone(String telephone);

    //查询指定月份之前的累计会员数量
    Integer findMemberCountByBeforeDate(String month);

    //今日新增会员数
    int findMemberCountByDate(String reportDate);

    //总会员数
    int findTotalCountMember();

    //指定日期后新增会员数）
    int findMemberCountAfterDate(String date);
}
