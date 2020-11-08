package com.jd.health.pojo;

import java.util.Date;

/**
 * @Auther lxy
 * @Date
 */
public class ClearOrder {
    private Date orderDate;//预约日期
    private int count;//预约日期对应预约数量

    public ClearOrder() {
    }

    public ClearOrder(Date orderDate, int count) {
        this.orderDate = orderDate;
        this.count = count;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ClearOrder{" +
                "orderDate=" + orderDate +
                ", count=" + count +
                '}';
    }
}
