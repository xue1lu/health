package com.jd.health.dao;

import com.github.pagehelper.Page;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface RoleDao {

    //分页查询角色
    Page<Role> findPage(String queryString);

    //新增角色
    void add(Role role);

    //新增角色权限关系
    void addRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);

    //根据id查询角色
    Role findById(int id);

    //根据角色id查询关联的权限id
    List<Integer> findRolePermissionIds(int id);

    //修改角色信息
    void update(Role role);

    //根据角色id删除旧的角色权限关系
    void deleteRolePermissionById(Integer id);

    //查询角色是否被用户关联
    int findUserRoleByRoleId(int id);

    //删除角色
    void deleteRoleById(int id)throws HealthException;

    //查询所有角色
    List<Role> findAll();
}
