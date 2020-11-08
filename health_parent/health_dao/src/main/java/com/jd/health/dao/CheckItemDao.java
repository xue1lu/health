package com.jd.health.dao;

import com.github.pagehelper.Page;
import com.jd.health.pojo.CheckItem;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface CheckItemDao {
    //查询所有检查项
    List<CheckItem> findAll();

    //新增检查项
    void add(CheckItem checkItem);

    //分页查询
    Page<CheckItem> findByCondition(String queryString);

    //删除检查项
    void deleteById(int id);

    //根据id查询检查项是否被绑定
    int findCountByCheckItemId(int id);

    //根据id查询检查项
    CheckItem findById(int id);

    //编辑检查项
    void update(CheckItem checkItem);

    //根据检查组id查询检查项
    List<CheckItem> findCheckItemListById(int checkGroupId);

}
