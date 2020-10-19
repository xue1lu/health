package com.jd.dao;

import com.jd.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther lxy
 * @Date 2020/9/19 18:31
 */
public interface UserDao {
    public abstract List<User> findAll();

    void save(User user);

    int update(User user);
    // 删除； @Param("id") 给参数取名，可以在映射文件中使用  #{id} 来获取参数值
    int deleteById(@Param("id") Integer id, String bbb);

    List<User> findByFirstName(String firstName);
//    动态 SQL 之if标签
    User findByUidAndUsername(User user);
//    动态 SQL 之set标签
    int updateByCondition(User user);
//    动态标签之foreach标签
    List<User> findByIds(@Param("ids") List<Integer> ids);
//SQL 片段
    List<User> findUserInfo();
}
