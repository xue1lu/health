package com.jd.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.jd.health.dao.MenuDao;
import com.jd.health.dao.RoleDao;
import com.jd.health.entity.PageResult;
import com.jd.health.exception.HealthException;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.Role;
import com.jd.health.service.MenuService;
import com.jd.health.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Service(interfaceClass = RoleService.class)

public class RoleServiceImpl implements RoleService {


    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;

    //分页查询角色
    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
        //使用pagehelper
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString().trim() + "%");
        }
        //查询
        Page<Role> page = roleDao.findPage(queryPageBean.getQueryString());

        return new PageResult<>(page.getTotal(), page.getResult());
    }

    //新增角色
    @Override
    @Transactional
    public void add(Integer[] permissionIds, Role role) {
        //新增角色
        roleDao.add(role);
        //新增角色与权限关系
        Integer roleId = role.getId();
        if (permissionIds != null) {
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(roleId, permissionId);
            }
        }
    }

    //根据id查询角色
    @Override
    public Role findById(int id) {
        return roleDao.findById(id);
    }

    //根据角色id查询关联的权限id
    @Override
    public List<Integer> findRolePermissionIds(int id) {
        return roleDao.findRolePermissionIds(id);
    }

    //编辑角色
    @Override
    @Transactional
    public void update(Integer[] permissionIds, Role role) {

        //修改角色信息
        roleDao.update(role);
        //根据角色id删除旧的角色权限关系
        roleDao.deleteRolePermissionById(role.getId());
        //建立新的角色权限关系
        if (permissionIds != null) {
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(role.getId(), permissionId);
            }
        }
    }

    //根据id删除角色
    @Override
    @Transactional
    public void deleteById(int id)throws HealthException {
        //查询角色是否被用户关联
        int count = roleDao.findUserRoleByRoleId(id);
        if (count > 0) {
            //角色被用户关联
            throw new HealthException("该角色已被用户关联,不能删除");
        }
        //删除角色关联权限关系
        roleDao.deleteRolePermissionById(id);
        //删除角色
        roleDao.deleteRoleById(id);
    }

    //查询所有角色

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }


    //添加角色
    @Override
    @Transactional
    public void addNew(Integer[] firstIds, Integer[] secondIds, Integer[] permissionIds, Role role) {
        //新增角色
        roleDao.add(role);
        //获得roleId
        Integer roleId = role.getId();
        //新增权限角色关系
        if (permissionIds != null) {
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(roleId, permissionId);
            }
        }
        //新增角色菜单关系
        if (firstIds != null) {
            for (Integer firstId : firstIds) {
                roleDao.addRoleMenu(roleId, firstId);
            }
        }
        //新增二级菜单
        if (secondIds != null) {
            for (Integer secondId : secondIds) {
                roleDao.addRoleMenu(roleId, secondId);
                //根据二级id查询查询父id
                Integer parentId = menuDao.findParentId(secondId);
                List<Integer> firstList = Arrays.asList(firstIds);
                if (!firstList.contains(parentId)) {
                    roleDao.addRoleMenu(roleId, parentId);
                }
            }
        }
    }



    //查询所有一级菜单id
    @Override
    public List<Integer> findRoleIds(int id) {
        return roleDao.findRoleIds(id);
    }

    //更新角色
    @Override
    @Transactional
    public void updateNew(Integer[] firstIds, Integer[] secondIds, Integer[] permissionIds, Role role) {

        //修改角色信息
        roleDao.update(role);
        Integer roleId = role.getId();
        //根据角色id删除旧的角色权限关系
        roleDao.deleteRolePermissionById(roleId);
        //建立新的角色权限关系
        if (permissionIds != null) {
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(roleId, permissionId);
            }
        }
        //根据角色删除旧的菜单信息
        roleDao.deleteRoleMenuById(roleId);
        //建立新的角色权限关系
        //新增角色菜单关系
        if (firstIds != null) {
            for (Integer firstId : firstIds) {
                roleDao.addRoleMenu(roleId, firstId);
            }
        }
        //新增二级菜单
        if (secondIds != null) {
            for (Integer secondId : secondIds) {
                roleDao.addRoleMenu(roleId, secondId);
                //根据二级id查询查询父id
                Integer parentId = menuDao.findParentId(secondId);
                List<Integer> firstList = Arrays.asList(firstIds);
                if (!firstList.contains(parentId)) {
                    roleDao.addRoleMenu(roleId, parentId);
                }
            }
        }
    }

    //删除角色
    @Override
    public void deleteByIdNew(int id) {
        //查询角色是否被用户关联
        int count = roleDao.findUserRoleByRoleId(id);
        if (count > 0) {
            //角色被用户关联
            throw new HealthException("该角色已被用户关联,不能删除");
        }
        //删除角色关联权限关系
        roleDao.deleteRolePermissionById(id);
        //删除角色菜单关系
        //根据角色删除旧的菜单信息
        roleDao.deleteRoleMenuById(id);
        //删除角色
        roleDao.deleteRoleById(id);
    }


    //根据id查询勾选的菜单
    @Override
    public List<Integer> findMenuIds(int id) {
        return roleDao.findMenuIds(id);
    }


    //添加角色
    @Override
    @Transactional
    public void addRole(Integer[] permissionIds, Integer[] menuIds, Role role) {
        //新增角色
        roleDao.add(role);
        //新增角色与权限关系
        Integer roleId = role.getId();
        if (permissionIds != null) {
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(roleId, permissionId);
            }
        }
        //新增角色菜单关系
        if (menuIds != null) {
            for (Integer menuId : menuIds) {
                roleDao.addRoleMenu(roleId, menuId);
            }
        }
    }

    //更新角色
    @Override
    public void updateRole(Integer[] permissionIds, Integer[] menuIds, Role role) {
        //修改角色信息
        roleDao.update(role);
        //根据角色id删除旧的角色权限关系
        roleDao.deleteRolePermissionById(role.getId());
        //建立新的角色权限关系
        if (permissionIds != null) {
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(role.getId(), permissionId);
            }
        }
        //根据角色id删除旧的角色权限关系
        roleDao.deleteRoleMenuById(role.getId());
        //新增角色菜单关系
        if (menuIds != null) {
            for (Integer menuId : menuIds) {
                roleDao.addRoleMenu(role.getId(), menuId);
            }
        }
    }

    //删除角色
    @Override
    @Transactional
    public void deleteRoleById(int id) throws HealthException {
        //查询角色是否被用户关联
        int count = roleDao.findUserRoleByRoleId(id);
        if (count > 0) {
            //角色被用户关联
            throw new HealthException("该角色已被用户关联,不能删除");
        }
        //删除角色关联权限关系
        roleDao.deleteRolePermissionById(id);
        //删除角色菜单关系
        //根据角色删除旧的菜单信息
        roleDao.deleteRoleMenuById(id);
        //删除角色
        roleDao.deleteRoleById(id);
    }


}
