package com.jd.health.service;

import com.jd.health.entity.PageResult;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.Permission;
import com.jd.health.pojo.QueryPageBean;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */

public interface PermissionService {
    //分页查询权限
    PageResult<Permission> findPage(QueryPageBean queryPageBean);

    //新增权限
    void add(Permission permission);

    //根据id查询权限
    Permission findById(int id);

    //编辑权限
    void update(Permission permission);

    //根据权限id查询角色权限表
    int findRolePermissionByPermissionId(int id);

    //删除权限
    void deleteById (int id) throws HealthException;

    //查询所有权限信息
    List<Permission> findAll();
}
