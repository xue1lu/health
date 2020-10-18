package com.jd.service.impl;

import com.jd.dao.ItemsDao;
import com.jd.pojo.Items;
import com.jd.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Service("itemsService")

public class ItemsServiceImpl implements ItemsService {
    //调用dao层
    @Autowired
    private ItemsDao itemsDao;
    @Override
    public List<Items> findAll() {
        System.out.println("service层调用dao层findAll");
        return itemsDao.findAll();
    }

    @Override
    public int save(Items items) {
        System.out.println("service层调用dao层save");

        return itemsDao.save(items);
    }

}
