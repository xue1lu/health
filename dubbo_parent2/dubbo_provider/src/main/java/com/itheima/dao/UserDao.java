package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/21
 */
public interface UserDao {
    User findById(int id);
}
