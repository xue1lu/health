package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.PageResult;
import com.jd.health.entity.Result;
import com.jd.health.pojo.CheckGroup;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    //分页查询所有检查组的方法
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用查询方法
        PageResult<CheckGroup> pageResult=checkGroupService.findPage(queryPageBean);
        //响应前端
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);
    }

    //新增检查项的方法
    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        checkGroupService.add(checkGroup,checkitemIds);
        //响应
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //根据检查组id查询检查组的方法
    @GetMapping("/findById")
    public Result findById(int checkgroupId) {
        //调用服务
        CheckGroup checkGroup = checkGroupService.findById(checkgroupId);
        //响应
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
    }

    //根据检查组id查询关联的检查项id
    @GetMapping("/findCheckGroupCheckItemId")
    public Result findCheckGroupCheckItemId(int checkgroupId) {
        //调用服务
        List<Integer> checkitemIds=checkGroupService.findCheckGroupCheckItemId(checkgroupId);
        //响应
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkitemIds);
    }

    //根据检查组id更新检查组
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupService.update(checkGroup, checkitemIds);
        //响应
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //根据检查组id删除检查组
    @PostMapping("/deleteById")
    public Result deleteById(int checkgroupId) {
        checkGroupService.deleteById(checkgroupId);
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询所有检查组
    @GetMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        //响应
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
    }




}
