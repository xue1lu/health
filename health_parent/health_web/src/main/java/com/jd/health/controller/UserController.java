package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.PageResult;
import com.jd.health.entity.Result;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/getUsername")
    public Result getUsername() {
        //获取用户登录的认证信息
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获得用户名
        String username = loginUser.getUsername();
        //返回
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

    @RequestMapping("/loginSuccess")
    public Result loginSuccess() {
        //返回
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
    @RequestMapping("/loginFail")
    public Result loginFail() {
        //返回
        return new Result(false, MessageConstant.LOGIN_FAILED);
    }

    //分页查询用户
    @PostMapping("/findPage")
    public Result findUserByPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<com.jd.health.pojo.User> pageResult=userService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, pageResult);
    }

    //添加用户
    @PostMapping("/add")
    public Result add(Integer[] roleIds, @RequestBody com.jd.health.pojo.User user) {
        //加密密码
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword().trim()));
        userService.add(roleIds, user);
        return new Result(true, MessageConstant.ADD_USER_SUCCESS);
    }

    //查询用户
    @GetMapping("/findById")
    public Result findById(int id) {
        com.jd.health.pojo.User user=userService.findById(id);
        user.setPassword("1234");
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, user);
    }

    //查询用户角色关系
    @GetMapping("/findUserRoleIds")
    public Result findUserRoleIds(int id) {
        List<Integer> roleIds = userService.findUserRoleIds(id);
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roleIds);
    }
    //编辑用户
    @PostMapping("/update")
    public Result update(Integer[] roleIds, @Validated @RequestBody com.jd.health.pojo.User user) {
            //密码加密
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword().trim()));

        userService.update(roleIds, user);
        return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
    }

    //删除用户
    @PostMapping("/deleteById")

    public Result deleteById(int id) {
        userService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }
}
