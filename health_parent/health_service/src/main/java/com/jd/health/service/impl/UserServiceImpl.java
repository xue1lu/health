package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jd.health.dao.UserDao;
import com.jd.health.entity.PageResult;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.User;
import com.jd.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = UserService.class)

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    //分页查询用户
    @Override
    public PageResult<User> findPage(QueryPageBean queryPageBean) {
        //设置分页查询条件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //根据条件查询
        Page<User> pages = userDao.findUserByCondition(queryPageBean.getQueryString());
        return new PageResult<>(pages.getTotal(), pages.getResult());
    }


    //新增用户
    @Override
    @Transactional
    public void add(Integer[] roleIds, User user) {

        //新增用户
        userDao.add(user);
        //新增用户角色关系
        if (null != roleIds) {
            for (Integer roleId : roleIds) {
                userDao.addUserRole(user.getId(), roleId);
            }
        }
    }

    //查询用户
    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    //查询用户角色关系
    @Override
    public List<Integer> findUserRoleIds(int id) {
        return userDao.findUserRoleIds(id);
    }

    //编辑用户
    @Override
    @Transactional
    public void update(Integer[] roleIds, User user) {

        //编辑用户
        userDao.update(user);
        //删除旧的用户角色关系
        userDao.deleteUserRoleByUserId(user.getId());
        //建立新的用户角色关系
        if (null != roleIds) {
            for (Integer roleId : roleIds) {
                userDao.addUserRole(user.getId(), roleId);
            }
        }
    }

    //删除用户
    @Override
    @Transactional
    public void deleteById(int id) {
        //删除用户角色关系
        userDao.deleteUserRoleByUserId(id);
        //删除用户
        userDao.deleteUserById(id);
    }
}
