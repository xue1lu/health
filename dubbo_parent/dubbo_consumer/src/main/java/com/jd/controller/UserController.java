package com.jd.controller;

import com.jd.pojo.User;
import com.jd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findById")
    public User findById(int id) {
        //调用接口服务
        User user = userService.findById(id);
        // 要返回json给前端，必须有@ResponseBody，或类上有@RestController
        // 另外springmvc默认使用jaskson来解析java对象为json字符串，注意引入jaskson的依赖
        return user;//返回json格式
    }
}
