package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jd.health.dao.PermissionDao;
import com.jd.health.entity.PageResult;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.Permission;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = PermissionService.class)

public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    //分页查询权限
    @Override
    public PageResult<Permission> findPage(QueryPageBean queryPageBean) {
        //使用pagehelper组件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString().trim() + "%");
        }
        Page<Permission> page = permissionDao.findPage(queryPageBean.getQueryString());
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    //新增权限
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    //根据id查询权限
    @Override
    public Permission findById(int id) {
        return  permissionDao.findById(id);
    }

    //编辑权限
    @Override
    public void update(Permission permission) {
        permissionDao.update(permission);
    }

    //根据权限id查询角色权限表
    @Override
    public int findRolePermissionByPermissionId(int id) {
        return permissionDao.findRolePermissionByPermissionId(id);
    }

    //删除权限
    @Override
    public void deleteById(int id)throws HealthException {
        permissionDao.deleteById(id);
    }

    //查询所有权限信息
    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }
}
