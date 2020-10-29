package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.Result;
import com.jd.health.pojo.Setmeal;
import com.jd.health.service.SetmealService;
import com.jd.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    private SetmealService setmealService;

    //查询所有套餐
    @GetMapping("/getSetmeal")
    public Result getSetmeal() {
        List<Setmeal> setmealList = setmealService.findAll();
        //缺少图片完整路径,进行拼接
        setmealList.forEach(s->{s.setImg(QiNiuUtils.DOMAIN+s.getImg());});
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmealList);
    }

    //根据套餐id显示检查套餐页详情,方式一
    @GetMapping("/findDetailById")
    public Result findDetailById(@RequestParam("id") int setmealId) {
        Setmeal setmeal = setmealService.findDetailById(setmealId);
        //设置回显的图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        //响应
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmeal);
    }
    //根据套餐id显示检查套餐页详情,方式二
    @GetMapping("/findDetailById2")
    public Result findDetailById2(@RequestParam("id") int setmealId) {
        Setmeal setmeal = setmealService.findDetailById2(setmealId);
        //设置回显的图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        //响应
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmeal);
    }

    //根据套餐id显示检查套餐页详情,方式二
    @GetMapping("/findDetailById3")
    public Result findDetailById3(@RequestParam("id") int setmealId) {
        Setmeal setmeal = setmealService.findDetailById3(setmealId);
        //设置回显的图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        //响应
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmeal);
    }

    //根据套餐id查询套餐
    @PostMapping("/findById")
    public Result findById(@RequestParam("id") int setmealId) {
        Setmeal setmeal = setmealService.findById(setmealId);
        //设置图片回显路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        //响应
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

}
