package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jd.health.dao.MenuDao;
import com.jd.health.dao.RoleDao;
import com.jd.health.dao.UserDao;
import com.jd.health.pojo.Menu;
import com.jd.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    //查询一级菜单
    @Override
    public List<Menu> findAllMenu() {
        List<Menu> parentMenu = menuDao.findFirstMenu();
        List<Menu> childrenMenu = menuDao.findSecondMenu();
            for (Menu menu1 : parentMenu) {
                Integer menu1Id = menu1.getId();
                for (Menu menu2 : childrenMenu) {
                    if (menu2.getParentMenuId() == menu1Id) {
                        menu1.getChildren().add(menu2);
                    }
                }
            }
        return parentMenu;
    }

    //查询一级菜单
    @Override
    public List<Menu> findFirstMenu() {
        return menuDao.findFirstMenu();
    }

    //根据一级菜单id查询二级菜单
    @Override
    public List<Menu> findSecondMenu() {
        return menuDao.findSecondMenu();

    }

    //根据用户名查询菜单
    @Override
    public List<Menu> findMenuByLoginUsername(String loginUsername) {
        //根据用户名查询一级菜单
        List<Menu> menuList1 = menuDao.findFirstMenuByUsername(loginUsername);
        //根据用户名查询二级菜单
        List<Menu> menuList2 = menuDao.findSecondMenuByUsername(loginUsername);
        if (menuList1 != null) {
            for (Menu menu : menuList1) {
                Integer menuId = menu.getId();
                if (menuList2 != null) {
                    for (Menu menu1 : menuList2) {
                        if (menu1.getParentMenuId() == menuId) {
                            menu.getChildren().add(menu1);
                        }
                    }
                }
            }
        }
        return menuList1;
    }


}
