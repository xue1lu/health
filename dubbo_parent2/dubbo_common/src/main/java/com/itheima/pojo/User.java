package com.itheima.pojo;

import java.io.Serializable;

/**
 * <p>
 * 实体类将来要通过远程调用的，必须实现序列化
 * 如果属性为基础数据类型，要使用它的包装类（有实现序列化），防止在远程调用过程中数据丢失
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/21
 */
public class User implements Serializable {

    private Integer id;
    private String username;
    private Integer age;

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", age=" + age +
            '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
