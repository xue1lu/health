package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.PageResult;
import com.jd.health.entity.Result;
import com.jd.health.pojo.CheckItem;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.service.CheckItemService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

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
    public Result add(@RequestBody CheckItem checkItem) {
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
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        //响应前端
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
    }
}
