package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jd.health.constant.MessageConstant;
import com.jd.health.dao.CheckItemDao;
import com.jd.health.entity.PageResult;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.CheckItem;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.service.CheckItemService;
import com.jd.health.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {
    //调用dao层
    @Autowired
    private CheckItemDao checkItemDao;

    //查询所有检查项
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    //新增检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //分页查询
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //mapper接口方式查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //如果有查询条件则拼接
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString().trim() + "%");
        }
        //根据条件查询
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        //返回结果
        return new PageResult<CheckItem>(page.getTotal(), page.getResult());
    }

    //删除检查项
    @Override
    public void deleteById(int id) throws HealthException {
        //判断该id对应的检查项是否被检查组使用,若被使用则不能删除
        int count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            //走到这里表示被检查组使用了
            throw new HealthException(MessageConstant.CHECKITEM_IN_USE);
        }
        //走到这里表示该检查项没有被使用
        checkItemDao.deleteById(id);
    }

    //根据id查询检查项
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    //编辑检查项
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

}
