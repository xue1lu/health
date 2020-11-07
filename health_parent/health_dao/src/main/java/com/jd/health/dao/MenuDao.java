package com.jd.health.dao;

import com.jd.health.pojo.Menu;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface MenuDao {
    //查询一级菜单
    List<Menu> findFirstMenu();

    //查询二级菜单
    List<Menu> findSecondMenu();

    //查询所有菜单信息
    List<Menu> findAllMenu();

    //根据一级菜单id查询二级菜单
    Menu findSecondMenuByFirstId(Integer firstId);

    //查询父菜单
    Integer findParentId(Integer secondId);


    //根据姓名查询一级菜单
    List<Menu> findFirstMenuByUsername(String loginUsername);

    //根据姓名查询二级菜单
    List<Menu> findSecondMenuByUsername(String loginUsername);
}
