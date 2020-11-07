package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.Result;
import com.jd.health.pojo.Menu;
import com.jd.health.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;


    //查询所有菜单信息
    @GetMapping("/findAllMenu")
    public Result findAllMenu() {
        List<Menu> data2 = menuService.findAllMenu();
        return new Result(true, MessageConstant.GET_ALLMENU_SUCCESS, data2);
    }

    //查询一级菜单
    @GetMapping("/findFirstMenu")
    public Result findFirstMenu() {
        List<Menu> menuData1 = menuService.findFirstMenu();
        return new Result(true, MessageConstant.GET_FIRSTMENU_SUCCESS, menuData1);
    }
    //查询二级菜单
    @GetMapping("/findSecondMenu")
    public Result findSecondMenu() {
        List<Menu> menuData2 = menuService.findSecondMenu();
        return new Result(true, MessageConstant.GET_FIRSTMENU_SUCCESS, menuData2);
    }

    //根据用户名查询菜单
    @GetMapping("/findMenuByLoginUsername")
    public Result findMenuByLoginUsername(String loginUsername) {
        List<Menu> menuList = menuService.findMenuByLoginUsername(loginUsername);
        return new Result(true, MessageConstant.GET_ALLMENU_SUCCESS, menuList);

    }

}
