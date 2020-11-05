package com.jd.health.dao;

import com.github.pagehelper.Page;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface UserDao {

    //根据用户名查询用户权限角色
    User findByUsername(String username);

    //根据条件查询用户
    Page<User> findUserByCondition(QueryPageBean queryPageBean);

    Page<User> findUserByCondition(String queryString);

    //新增用户
    void add(User user);

    //新增用户角色关系
    void addUserRole(@Param("userId") Integer id,@Param("roleId")  Integer roleId);

    //查询用户
    User findById(int id);

    //查询用户角色关系
    List<Integer> findUserRoleIds(int id);

    //编辑用户
    void update(User user);

    //删除旧的用户角色关系
    void deleteUserRoleByUserId(Integer id);

    //删除用户
    void deleteUserById(int id);
}
