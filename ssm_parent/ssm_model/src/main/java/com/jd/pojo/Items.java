package com.jd.pojo;

import lombok.*;

import java.util.Date;

/**
 * @Auther lxy
 * @Date
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Items {
    private Integer id;
    private String name;
    private Float price;
    private String pic;
    private Date createtime;
    private String detail;


}
