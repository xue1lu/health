package com.jd;

import com.jd.bean.Product;
import com.jd.dao.ProductDao;
import com.jd.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @Auther lxy
 * @Date 2020/9/20 18:05
 */
public class MybatisProduct {

    @Test
    public void testFindAll() {
     //初始化获得sqlsession对象
        SqlSession sqlSession = SqlSessionFactoryUtils.getSqlSession();
        //获得接口的代理对象
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        //执行操作
        List<Product> productList = productDao.findAll();
        System.out.println(productList);
        //关闭sqlsession
        SqlSessionFactoryUtils.close(sqlSession);
    }

    @Test
    public void testfindAllByAlias() {

        //初始化获得sqlsession对象
        SqlSession sqlSession = SqlSessionFactoryUtils.getSqlSession();
        //获得接口代理对象
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        //执行数据库操作
        List<Product> productDaoAllByAlias = productDao.findAllByAlias();
        System.out.println(productDaoAllByAlias);
        //关闭sqlsession
        SqlSessionFactoryUtils.close(sqlSession);
    }

    @Test
    public void testfindAllBySetyins() {
        //获得初始化sqlsession
        SqlSession sqlSession = SqlSessionFactoryUtils.getSqlSession();
        //获得接口代理对象
        ProductDao productDao = sqlSession.getMapper(ProductDao.class);
        //执行数据库操作
        List<Product> productDaoAllBySetyins = productDao.findAllBySetyins();
        System.out.println(productDaoAllBySetyins);
        //关闭sqlsession
        SqlSessionFactoryUtils.close(sqlSession);
    }

}
