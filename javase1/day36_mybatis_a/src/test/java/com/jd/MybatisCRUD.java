package com.jd;

import com.jd.bean.User;
import com.jd.dao.UserDao;
import com.jd.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther lxy
 * @Date 2020/9/19 21:12
 */
public class MybatisCRUD {
//新增用户
    @Test
    public void testInsert() throws IOException {
        //1.加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        //2.得到sqlsession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //3.得到sqlsession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4.获取接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //5.调用方法执行数据库操作
        User user = new User();
        user.setUsername("zs");
        user.setSex("男");
        userDao.save(user);
        System.out.println(user);
        // mybatis默认不会自动提交事务，需要手动处理
        sqlSession.commit();
        //关闭sqlsession
        sqlSession.close();

    }
//修改用户
    @Test
    public void testUpdate() throws IOException {
        //1.加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        //2.获取sqlsession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //3.获得sqlsession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4.获取接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //5.调用方法执行数据库操作
        User user = new User();
        user.setSex("女");
        user.setUid(5);
        user.setAddress("上海");
        int update = userDao.update(user);
        System.out.println(update);
        //6.提交事务,mybatis默认不会自动提交事务
        sqlSession.commit();
        //7.关闭sqlsession
        sqlSession.close();
    }
//删除用户
    @Test
    public void testDeleteById() throws IOException {
        //1.加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        //2.获得sqlsession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //3.获得sqlsession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4.获得接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //5.执行数据库操作
        System.out.println(userDao.deleteById(4, "bbb"));
        //6.提交事务
        sqlSession.commit();
        //7.关闭session
        sqlSession.close();
    }
//模糊查询
    @Test
    public void testLikeQuery() throws IOException {
        //1.加载核心配置文件
        Reader resourceAsReader = Resources.getResourceAsReader("mybatis-config.xml");
        //2.获得sqlsession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsReader);
        //3.获得sqlsession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4.获得接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //5.执行操作
        System.out.println(userDao.findByFirstName("王%"));
        //6.关闭sqlsession
        sqlSession.close();
    }
//动态 SQL 之if标签
    @Test
    public void testfindByUidAndUsername() {
        //初始化获得sqlsession对象
        SqlSession sqlSession = SqlSessionFactoryUtils.getSqlSession();
        //获得接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //执行操作
        User user = new User();
        user.setUid(2);
        user.setUsername("ls");
        System.out.println(userDao.findByUidAndUsername(user));
        //关闭sqlsession
        SqlSessionFactoryUtils.close(sqlSession);
    }

//动态 SQL 之set标签
    @Test
    public void testupdateByCondition() throws ParseException {
        //初始化获得sqlsession对象
        SqlSession sqlSession = SqlSessionFactoryUtils.getSqlSession();
        //获得接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //执行操作
        User user = new User();
        user.setUid(5);
        user.setUsername("zl");
        user.setSex("男");
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-24"));
        System.out.println(userDao.updateByCondition(user));

        //提交事务
        sqlSession.commit();
        //关闭sqlsession
        SqlSessionFactoryUtils.close(sqlSession);
    }
//知识点-动态标签之foreach标签
    @Test
    public void testfindByIds() throws Exception {
        //初始化获得sqlsession对象
        SqlSession sqlSession = SqlSessionFactoryUtils.getSqlSession();
        //获得接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //执行操作
        Integer[] arr = {1, 3, 5, 7};
        List<Integer> integerList = Arrays.asList(arr);
        List<User> userList = userDao.findByIds(integerList);
        System.out.println(userList);
        //关闭sqlsession
        SqlSessionFactoryUtils.close(sqlSession);
    }
//知识点-SQL 片段
    @Test
    public void testfindUserInfo() throws Exception {
        //初始化获得sqlsession对象
        SqlSession sqlSession = SqlSessionFactoryUtils.getSqlSession();
        //获得接口的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //执行操作
        List<User> userList = userDao.findUserInfo();
        System.out.println(userList);
        //关闭sqlsession
        SqlSessionFactoryUtils.close(sqlSession);
    }

}
