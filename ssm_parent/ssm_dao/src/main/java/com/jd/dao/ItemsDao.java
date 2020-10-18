package com.jd.dao;


import com.jd.pojo.Items;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Mapper
public interface ItemsDao {
    //查询所有
    List<Items> findAll();

    //增加
    int save(Items items);

}
