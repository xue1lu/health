package com.jd.health.service;

import com.jd.health.entity.PageResult;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.User;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface UserService {

    //查询用户权限信息
    User findByUsername(String username);

    //分页查询用户信息
    PageResult<User> findPage(QueryPageBean queryPageBean);

    //添加用户
    void add(Integer[] roleIds, User user);

    //查询用户
    User findById(int id);

    ///查询用户角色关系
    List<Integer> findUserRoleIds(int id);

    //编辑用户
    void update(Integer[] roleIds, User user);

    //删除用户
    void deleteById(int id);
}
