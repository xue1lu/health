package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jd.health.constant.MessageConstant;
import com.jd.health.dao.SetmealDao;
import com.jd.health.entity.PageResult;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.Setmeal;
import com.jd.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    //分页查询
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //使用pagehelper
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //修改查询条件保证模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString().trim() + "%");
        }
        Page<Setmeal> pages = setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(pages.getTotal(), pages.getResult());
    }

    //新增套餐
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增套餐
        setmealDao.add(setmeal);
        //获取套餐id
        Integer setmealId = setmeal.getId();
        //判断是否勾选检查组
        if (checkgroupIds != null) {
            //循环遍历
            for (Integer checkgroupId : checkgroupIds) {
                //添加到关联列表,中间表
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }

    }

    //根据id查询套餐
    @Override
    public Setmeal findById(int setmealId) {
        return setmealDao.findById(setmealId);
    }

    //根据套餐id查询勾选关联的检查组ids
    @Override
    public List<Integer> findSetmealCheckGroupBySetmealId(int setmealId) {
        return setmealDao.findSetmealCheckGroupBySetmealId(setmealId);
    }

    //更新套餐
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //更新套餐表
        setmealDao.update(setmeal);
        //删除旧的关联检查组信息
        setmealDao.deleteSetmealCheckGroupBySetmealId(setmeal.getId());
        //建立新的关联关系
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
    }

    //删除套餐
    @Override
    @Transactional
    public void deleteById(int setmealId) throws HealthException{

        //判断是否该套餐被订单关联
        int count=setmealDao.findOrderCountBysetmealId(setmealId);
        if (count > 0) {
            throw new HealthException(MessageConstant.CHECKSETMEAL_IN_USE);
        }
        //删除关联检查组
        setmealDao.deleteSetmealCheckGroupBySetmealId(setmealId);
        //删除检查套餐
        setmealDao.deleteById(setmealId);

    }

    //查询数据库中所有图片名称
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }

}
