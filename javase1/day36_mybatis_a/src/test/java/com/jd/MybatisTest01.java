package com.jd;

import com.jd.bean.User;
import com.jd.dao.UserDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Auther lxy
 * @Date 2020/9/19 20:02
 */
public class MybatisTest01 {

    @Test
    public void test01() throws IOException {
        //1.使用mybatis
        //加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        //2.构建一个sqlsession对象(工厂模式)
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        /*
        * 可以选择使用指定id的数据库实例：is 表示核心配置文件输入流， 第二个字符串类型参数是配置environment的id属性值。
        * new SqlSessionFactoryBuilder().build(is, "test");
        * */
        //3.获取sqlsession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4.获取接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //5.调用方法执行数据库操作
        List<User> userList = userDao.findAll();
        System.out.println(userList);

        //6.释放资源关闭sqlsession
        sqlSession.close();


    }
}
