package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jd.health.dao.CheckItemDao;
import com.jd.health.entity.PageResult;
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


    /*  //分页查询方式1
      @Override
      public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {

          Long total = checkItemDao.findByCondition(queryPageBean);
          System.out.println("total"+total);
          //查询当前页码展示的行记录
          List<CheckItem> rows = checkItemDao.findCurrnet(queryPageBean);
          return new PageResult<CheckItem>(total, rows);

      }*/
    //分页查询
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //mapper接口方式查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //如果有查询条件则拼接
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //根据条件查询
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        //返回结果
        return new PageResult<CheckItem>(page.getTotal(), page.getResult());
    }

}
