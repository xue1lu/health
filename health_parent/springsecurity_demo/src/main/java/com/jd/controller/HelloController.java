package com.jd.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther lxy
 * @Date
 */
@Controller
@RequestMapping("/hello")

public class HelloController {
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('add')")//表示用户必须拥有add权限才能调用当前方法
    public String add() {
        System.out.println("add");
        return null;
    }

    @RequestMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//表示用户必须具有role_admin角色
    public String update() {
        System.out.println("update");
        return null;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ABC')")//表示用户必须具有ABC角色
    public String delete() {
        System.out.println("delete");
        return null;
    }
}
