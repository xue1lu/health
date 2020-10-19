package com.jd.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther lxy
 * @Date 2020/9/20 17:40
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private Integer fId;
    private String fName;
    private Double fPrice;
}
