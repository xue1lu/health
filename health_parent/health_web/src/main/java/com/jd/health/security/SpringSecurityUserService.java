package com.jd.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.pojo.Permission;
import com.jd.health.pojo.Role;
import com.jd.health.pojo.User;
import com.jd.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Auther lxy
 * @Date
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户登录信息查询用户密码和权限集合
        User userInDb = userService.findByUsername(username);
        //获取密码和权限
        if (userInDb != null) {
            //获取密码
            String password = userInDb.getPassword();
            //创建权限集合保存角色和权限信息
            List<GrantedAuthority> authorityList= new ArrayList<GrantedAuthority>();
            //创建权限对象
            SimpleGrantedAuthority simpleGrantedAuthority = null;
            //获取用户角色
            Set<Role> roles = userInDb.getRoles();
            //遍历角色信息
            if (roles != null && roles.size() > 0) {
                for (Role role : roles) {
                    //获取角色
                    simpleGrantedAuthority = new SimpleGrantedAuthority(role.getKeyword());
                    authorityList.add(simpleGrantedAuthority);
                    //获取角色对应权限
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions != null && permissions.size() > 0) {
                        for (Permission permission : permissions) {
                            //获取权限
                            simpleGrantedAuthority= new SimpleGrantedAuthority(permission.getKeyword());
                            authorityList.add(simpleGrantedAuthority);
                        }
                    }
                }
            }
            //返回用户权限详情
            return new org.springframework.security.core.userdetails.User(username, password, authorityList);
        }
        return null;
    }
}
