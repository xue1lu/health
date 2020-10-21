package com.jd.dao;

import com.jd.pojo.User;

/**
 * @Auther lxy
 * @Date
 */
public interface UserDao {
    User findById(Integer id);
}
