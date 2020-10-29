package com.jd.health.dao;

import com.github.pagehelper.Page;
import com.jd.health.pojo.CheckGroup;
import com.jd.health.pojo.QueryPageBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface CheckGroupDao {

    //分页查询
    Page<CheckGroup> findByCondition(String queryString);

    //新增检查组
    void add(CheckGroup checkGroup);

    //更新检查组与检查项关联信息表
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    //根据检查组id查询检查组
    CheckGroup findById(int checkgroupId);

    //根据检查组id查询关联的检查项id
    List<Integer> findCheckGroupCheckItemId(int checkgroupId);

    //更新检查组
    void update(CheckGroup checkGroup);

    //根据检查组id删除关联的检查项
    void deleteCheckGroupCheckItemIdById(Integer id);

    //根据检查组的id查询对应的套餐
    int findSetmealCountBycheckgroupId(int checkgroupId);

    //根据检查组id删除检查组
    void deleteCheckgroupById(int checkgroupId);

    //查询所有检查组
    List<CheckGroup> findAll();

    //根据套餐id查询检查组
    List<CheckGroup> findCheckGroupListById(int setmealId);
}
