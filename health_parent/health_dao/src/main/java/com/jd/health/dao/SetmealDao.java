package com.jd.health.dao;

import com.github.pagehelper.Page;
import com.jd.health.pojo.CheckGroup;
import com.jd.health.pojo.CheckItem;
import com.jd.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface SetmealDao {
    //分页查询
    Page<Setmeal> findPage(String queryString);

    //新增套餐
    void add(Setmeal setmeal);

    //新增检查套餐时更新套餐检查组中间表
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    //根据id查询套餐
    Setmeal findById(int setmealId);

    //根据套餐id查询勾选关联的检查组ids
    List<Integer> findSetmealCheckGroupBySetmealId(int setmealId);

    //更新套餐
    void update(Setmeal setmeal);

    //根据套餐id删除关联的检查组信息
    void deleteSetmealCheckGroupBySetmealId(Integer setmealId);

    //根据套餐id查询关联订单
    int findOrderCountBysetmealId(int setmealId);

    //删除套餐
    void deleteById(int setmealId);

    //查询数据库中所有图片名称
    List<String> findImgs();

    //查询所有套餐
    List<Setmeal> findAll();

    //根据套餐id查询对应的检查组
    List<CheckGroup> findCheckGroupBySetmealId(int setmealId);

    //根据检查组id查询对应的检查项
    List<CheckItem> findCheckItemByCheckGroupId(Integer checkGroupId);

    //根据套餐id查询套餐详情2
    Setmeal findDetailById2(int setmealId);

    //根据套餐id查询套餐详情3
    Setmeal findDetailById3(int setmealId);
}
