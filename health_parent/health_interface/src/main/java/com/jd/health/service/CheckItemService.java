package com.jd.health.service;

import com.jd.health.entity.PageResult;
import com.jd.health.pojo.CheckItem;
import com.jd.health.pojo.QueryPageBean;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface CheckItemService {
    //查询所有检查项
    List<CheckItem> findAll();

    //新增检查项
    void add(CheckItem checkItem);

    //分页查询
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /*PageResult<CheckItem> findPage(QueryPageBean queryPageBean);*/
}
