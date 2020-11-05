package com.jd.health.dao;

import com.github.pagehelper.Page;
import com.jd.health.pojo.Permission;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface PermissionDao {
    //分页查询权限
    Page<Permission> findPage(String queryString);

    //新增权限
    void add(Permission permission);

     //根据id查询权限
    Permission findById(int id);

    //编辑权限
    void update(Permission permission);

    //根据权限id查询角色权限表
    int findRolePermissionByPermissionId(int id);

    //删除权限
    void deleteById (int id);

    //查询所有权限信息
    List<Permission> findAll();
}
