package com.jd.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jd.health.constant.MessageConstant;
import com.jd.health.dao.CheckGroupDao;
import com.jd.health.entity.PageResult;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.CheckGroup;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.List;


/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    //注入dao层
    @Autowired
    private CheckGroupDao checkGroupDao;

    //分页查询检查组的方法
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        //分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //如果有则设置查询条件为模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString().trim() + "%");
        }
        //调用dao
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        //封装返回分页查询结果
        return new PageResult<CheckGroup>(page.getTotal(), page.getResult());
    }

    //新增检查组的方法
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组
        checkGroupDao.add(checkGroup);
        //更新检查组于检查项关联的信息
        //获得新增检查组的id
        Integer checkGroupId = checkGroup.getId();
        if (checkitemIds != null) {
            //遍历检查项id
            for (Integer checkitemId : checkitemIds) {
                //调用方法
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);
            }
        }
    }

    //根据检查组id查询检查组
    @Override
    public CheckGroup findById(int checkgroupId) {
        return checkGroupDao.findById(checkgroupId);
    }

    //根据检查组id查询关联的检查项id
    @Override
    public List<Integer> findCheckGroupCheckItemId(int checkgroupId) {
        return checkGroupDao.findCheckGroupCheckItemId(checkgroupId);
    }

    //根据检查组id更新检查组
    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //更新检查组
        checkGroupDao.update(checkGroup);
        //删除检查组id关联的检查项信息
        checkGroupDao.deleteCheckGroupCheckItemIdById(checkGroup.getId());
        //更新检查组id关联的检查项信息
        if (checkitemIds != null) {
            //遍历id
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(), checkitemId);
            }
        }
    }

    ////根据检查组id删除检查组
    @Override
    @Transactional
    public void deleteById(int checkgroupId) throws HealthException {
        //首先查询该id对应的检查组是否关联的有套餐id
        int count = checkGroupDao.findSetmealCountBycheckgroupId(checkgroupId);
        if (count > 0) {
            //如果关联有套餐则不能删除
            throw new HealthException(MessageConstant.CHECKGROUP_IN_USE);
        }
        //删除检查组关联的检查项
        checkGroupDao.deleteCheckGroupCheckItemIdById(checkgroupId);
        //没有则删除检查组
        checkGroupDao.deleteCheckgroupById(checkgroupId);
    }

    //查询所有检查组
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

}
