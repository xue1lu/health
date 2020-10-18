package com.jd.service;

import com.jd.pojo.Items;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
public interface ItemsService {
    //查询所有的方法
    List<Items> findAll();

    //插入方法
    int save(Items items);
}
