package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.UserDao;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/21
 */
@Service(version = "2")
public class UserServiceImpl2 implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findById(int id) {
        System.out.println("UserServiceImpl22222222222222222222");
        return userDao.findById(id);
    }
}
