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


 /*   Long findByCondition(QueryPageBean queryPageBean);

    List<CheckItem> findCurrnet(QueryPageBean queryPageBean);*/
}
