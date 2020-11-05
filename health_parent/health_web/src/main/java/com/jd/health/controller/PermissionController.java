package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.PageResult;
import com.jd.health.entity.Result;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.Permission;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    //分页查询权限
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用业务层
        PageResult<Permission> pageResult = permissionService.findPage(queryPageBean);
        //响应
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, pageResult);
    }

    //新增权限
    @PostMapping("/add")
    public Result add(@RequestBody Permission permission) {
        permissionService.add(permission);
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    //根据id查询权限
    @GetMapping("/findById")
    public Result findById(int id) {
        Permission permission = permissionService.findById(id);
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permission);
    }

    //编辑权限
    @PostMapping("/update")
    public Result update(@RequestBody Permission permission) {
        permissionService.update(permission);
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
    }
    //删除权限
    @GetMapping("/deleteById")
    public Result deleteById(int id) {
        //查询该权限是否被角色关联
        int count = permissionService.findRolePermissionByPermissionId(id);
        if (count > 0) {
            throw new HealthException("权限正在被使用,不能删除");
        }
        permissionService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }
    //查询所有权限信息
    @GetMapping("/findAll")
    public Result findAll() {
        List<Permission> permissionList = permissionService.findAll();
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permissionList);
    }

}
