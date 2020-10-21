package com.jd.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * @Auther lxy
 * @Date
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User implements Serializable {
    private Integer id;
    private String username;
    private Integer age;
}
