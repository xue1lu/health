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
public class UserService1 implements UserDetailsService {

    //通过用户名加载用户信息user登录使用
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username:" + username);
        //通过用户名查询角色权限
        User userInDb = findByUsername(username);
        if (null != userInDb) {
            //如果用户存在,创建用户权限集合
            List <GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();
            //获取用户拥有的角色
            Set<Role> roles = userInDb.getRoles();
            //遍历角色
            if (roles != null && roles.size() > 0) {
                for (Role role : roles) {
                    //获取角色
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
                    authorities.add(grantedAuthority);
                    //获取角色下的权限
                    Set<Permission> permissions = role.getPermissions();
                    //遍历权限
                    if (permissions != null && permissions.size() > 0) {
                        for (Permission permission : permissions) {
                            //获取权限
                            grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                            authorities.add(grantedAuthority);
                        }
                    }
                }
            }
            //登录用户的认证信息,用户名,密码,权限集合
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username,"{noop}" + userInDb.getPassword(), authorities);
            return user;
        }
        return null;
    }

    private User findByUsername(String username) {
        //用户
        if ("admin".equals(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword("admin");
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
