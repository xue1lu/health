package com.jd;

import com.jd.service.ItemsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Auther lxy
 * @Date
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-service.xml")
public class ServiceTest {
    @Autowired
    private ItemsService itemsService;
    @Test
    public void testfindAll() {
        System.out.println("service执行findAll"+itemsService.findAll());
    }
}
