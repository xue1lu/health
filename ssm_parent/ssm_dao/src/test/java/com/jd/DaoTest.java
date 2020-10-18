package com.jd;

import com.jd.dao.ItemsDao;
import com.jd.pojo.Items;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @Auther lxy
 * @Date
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mybatis.xml")
public class DaoTest {
    @Autowired
    private ItemsDao itemsDao;
    @Test
    public  void testSave() {

        System.out.println("商品列表：：："+itemsDao.findAll());
        Items items = Items.builder().name("商品名称").price(16666f).createtime(new Date()).build();
        //执行操作
        itemsDao.save(items);

    }
}
