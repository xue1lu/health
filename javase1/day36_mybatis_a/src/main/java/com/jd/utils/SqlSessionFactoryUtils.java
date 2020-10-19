package com.jd.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.Null;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Auther lxy
 * @Date 2020/9/20 17:03
 */
public class SqlSessionFactoryUtils {

    //创建私有的sqlsession工厂对象
    private static SqlSessionFactory sqlSessionFactory=null;
    //创建静态代码块
    static{
        try {
            //加载核心配置文件
            InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
            //创建sqlsession工厂对象
            Configuration config;
            sqlSessionFactory=new SqlSessionFactoryBuilder().build(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //提供获得sqlsession的方法
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }

    //提供关闭sqlsession的方法
    public static void close(SqlSession sqlSession) {
        sqlSession.close();
    }
}
