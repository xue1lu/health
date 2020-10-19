package com.jd.dao;

import com.jd.bean.Product;

import java.util.List;

/**
 * @Auther lxy
 * @Date 2020/9/20 17:44
 */
public interface ProductDao {
    List<Product> findAll();
    List<Product> findAllByAlias();
    List<Product> findAllBySetyins();
}
