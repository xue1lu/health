package com.jd.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther lxy
 * @Date 2020/9/19 18:27
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    private Integer uid; //用户id
    private String username;// 用户姓名
    private String sex;// 性别
    private Date birthday;// 生日
    private String address;// 地址

}
