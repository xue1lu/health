package com.jd.service.impl;

import com.jd.dao.UserDao;
import com.jd.pojo.User;
import com.jd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Auther lxy
 * @Date
 */
public class UserServiceImpl implements UserService {

    //调用dao等
    @Autowired
    UserDao userDao;
    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }
}
