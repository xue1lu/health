package com.jd.health.service;

import com.jd.health.entity.PageResult;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.Role;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface RoleService {
    //分页查询角色
    PageResult<Role> findPage(QueryPageBean queryPageBean);

    //新增角色
    void add(Integer[] permissionIds, Role role);

    //根据id查询角色
    Role findById(int id);

    //根据角色id查询关联的权限id
    List<Integer> findRolePermissionIds(int id);

    //编辑角色
    void update(Integer[] permissionIds, Role role);

    //根据id删除角色
    void deleteById(int id);

    //查询所有角色
    List<Role> findAll();
}
