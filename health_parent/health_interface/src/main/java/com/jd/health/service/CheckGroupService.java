package com.jd.health.service;

import com.jd.health.entity.PageResult;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.CheckGroup;
import com.jd.health.pojo.QueryPageBean;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface CheckGroupService {

    //分页查询检查组
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    //新增检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    //根据检查组id查询检查组
    CheckGroup findById(int checkgroupId);

    //根据检查组id查询关联的检查项id
    List<Integer> findCheckGroupCheckItemId(int checkgroupId);

    //根据检查组id更新检查组
    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    //根据检查组id删除检查组
    void deleteById(int checkgroupId) throws HealthException;

    //查询所有检查组
    List<CheckGroup> findAll();
}
