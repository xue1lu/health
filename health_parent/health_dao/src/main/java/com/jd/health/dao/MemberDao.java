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
}
