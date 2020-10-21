package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/21
 */
@RestController
@RequestMapping("user")
public class UserController {

    /**
     * loadbalance 负载均衡
     * roundrobin 轮循
     */
    @Reference(version = "1", loadbalance = "roundrobin")
    private UserService userService;

    @GetMapping("findById")
    public User findById(int id){
        // 动态代理对象InvocationHandler 方法里做什么 调用服务端的方法
        // 远程调用 Socket(ip:port) -> 传数据给 服务端(ServerSocket(2088)) -> 传回给客户端
        User user = userService.findById(id);
        return user;
    }
}
