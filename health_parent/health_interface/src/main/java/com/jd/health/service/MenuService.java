package com.jd.health.service;

import com.jd.health.pojo.Menu;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface MenuService {
    //查询所有菜单信息
    List<Menu> findAllMenu();

    //查询一级菜单
    List<Menu> findFirstMenu();

    //查询二级菜单
    List<Menu> findSecondMenu();


    //根据用户名查询菜单
    List<Menu> findMenuByLoginUsername(String loginUsername);
}
