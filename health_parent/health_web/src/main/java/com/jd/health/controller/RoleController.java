package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.PageResult;
import com.jd.health.entity.Result;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.Role;
import com.jd.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用业务层
      PageResult<Role> pageResult= roleService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, pageResult);
    }

    //根据id查询角色
    @GetMapping("/findById")
    public Result findById(int id) {
        Role role=roleService.findById(id);
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, role);
    }

    //根据角色id查询关联的权限id
    @GetMapping("/findRolePermissionIds")
    public Result findRolePermissionIds(int id) {
       List<Integer> permissionIds= roleService.findRolePermissionIds(id);
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permissionIds);
    }

    //查询所有角色
    @GetMapping("/findAll")
    public Result findAll() {
        List<Role> roleList = roleService.findAll();
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roleList);
    }

    //获得所有菜单id
    @GetMapping("/findRoleIds")
    public Result findRoleIds(int id) {
        List<Integer> Ids = roleService.findRoleIds(id);
        return new Result(true, MessageConstant.GET_FIRSTMENU_SUCCESS, Ids);
    }

    //根据id查询勾选的菜单
    @GetMapping("/findMenuIds")
    public Result findMenuIds(int id) {
        List<Integer> menuIds = roleService.findMenuIds(id);
        return new Result(true, MessageConstant.GET_CHECK_SUCCESS, menuIds);
    }

    //增加角色
    @PostMapping("/addRole")
    public Result addRole(Integer[] permissionIds,Integer[] menuIds,@RequestBody Role role) {
        roleService.addRole(permissionIds, menuIds, role);
        return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
    }
    //更新角色
    @PostMapping("/updateRole")
    public Result updateRole(Integer[] permissionIds,Integer[] menuIds,@RequestBody Role role) {
        roleService.updateRole(permissionIds, menuIds, role);
        return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
    }

    //删除角色
    //根据id删除角色
    @PostMapping("/deleteRoleById")
    public Result deleteRoleById(int id) {
        roleService.deleteRoleById(id);
        return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
    }
}
