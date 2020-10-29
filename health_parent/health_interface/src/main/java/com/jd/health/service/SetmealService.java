package com.jd.health.service;

import com.jd.health.entity.PageResult;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.Setmeal;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface SetmealService {
    //分页查询套餐
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    //新增套餐
    Integer add(Setmeal setmeal, Integer[] checkgroupIds);


    ////根据id查询套餐
    Setmeal findById(int setmealId);

    //根据套餐id查询勾选关联的检查组ids
    List<Integer> findSetmealCheckGroupBySetmealId(int setmealId);


    //更新套餐
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    //删除套餐
    void deleteById(int setmealId) throws HealthException;

    //查询数据库中所有图片名称
    List<String> findImgs();

    //查询所有套餐
    List<Setmeal> findAll();

    //根据套餐id查询套餐详情
    Setmeal findDetailById(int setmealId);

    //根据套餐id查询套餐详情2
    Setmeal findDetailById2(int setmealId);
    //根据套餐id查询套餐详情3
    Setmeal findDetailById3(int setmealId);
}
