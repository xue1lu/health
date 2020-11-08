package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.PageResult;
import com.jd.health.entity.Result;
import com.jd.health.pojo.CheckItem;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    // 订阅 treeCache
    ///dubbo/ 接口包名/provides/ 解析 ip:port 接口 方法
    // forPath("/dubbo/com.itheim..CheckItemService/providers") ip:port findAll

    /*Proxy.newProxyInstance(CheckItemController.class.getClassLoader(), new Class[]{CheckItemService.class}, new InvocationHandler() {
           @Override
           public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               // 连接服务提供方
               Socket socket = new Socket(ip,port);
               socket.getOutputStream().write("findByAll".getBytes());
               InputStream inputStream = socket.getInputStream();
               inputStream.read() 字符流 // 获取服务端响应的结果
               // 反序列化
               return list;
           }
       });*/
    //注入service
    @Reference
    private CheckItemService checkItemService;

    //查询所有的方法
    @GetMapping("/findAll")
    public Result findAll() {
        List<CheckItem> checkItemList = checkItemService.findAll();
        //封装返回结果
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
    }

    //新增检查项的方法
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){

        checkItemService.add(checkItem);
        //返回结果
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

 /*   //分页查询
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckItem> pageResult=checkItemService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);
    }
*/

    //分页查询
    @PostMapping("/findPage")
  //  @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        //响应前端
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
    }

    //删除检查项
    @GetMapping("/deleteById")
    public Result deleteById(int id) {
        checkItemService.deleteById(id);
        //响应
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //根据id查询检查项
    @GetMapping("/findById")
    public Result findById(int id) {
        CheckItem checkItem=checkItemService.findById(id);
        //响应
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
    }

    //修改检查项
    @PostMapping("/update")
    public Result update(@Validated @RequestBody CheckItem checkItem) {
        checkItemService.update(checkItem);
        //响应
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
