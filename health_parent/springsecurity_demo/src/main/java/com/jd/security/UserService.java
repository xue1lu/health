package com.jd.security;

import com.jd.health.pojo.Permission;
import com.jd.health.pojo.Role;
import com.jd.health.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther lxy
 * @Date
 */
public class UserService implements UserDetailsService {

    //通过用户名加载用户信息user登录使用
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username1:" + username);
        //查询用户
        User userInDb = findByUsername(username);
        //创建权限集合
        List<GrantedAuthority> authorityList= new ArrayList<GrantedAuthority>();
       //测试其他认证方式
        GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorityList.add(ga);
        ga=new SimpleGrantedAuthority("add");
        authorityList.add(ga);
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, userInDb.getPassword(), authorityList);

        return user;
    }

    private User findByUsername(String username) {
        System.out.println("username2："+username);
        //用户
        if ("admin".equals(username)) {
            User user = new User();
            user.setUsername(username);
            //使用encoder加密
            user.setPassword("$2a$10$kTOJfJkpDlFjCLIM4/UOY.plmYkFcC1KBLLYwKA6xlQMPFkao8LnW");
            //角色
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            //权限
            Permission permission = new Permission();
            permission.setName("ADD_CHECKITEM");
            //添加角色具有的权限
            role.getPermissions().add(permission);
            //创建用户拥有角色集合
            Set<Role> roleSet= new HashSet<Role>();
            roleSet.add(role);
            //添加用户的角色
            user.setRoles(roleSet);
            return user;
        }
        return null;
    }
}
