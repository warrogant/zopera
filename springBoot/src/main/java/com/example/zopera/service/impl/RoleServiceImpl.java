package com.example.zopera.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zopera.entity.Role;
import com.example.zopera.entity.RoleMenu;
import com.example.zopera.mapper.RoleMapper;
import com.example.zopera.mapper.RoleMenuMapper;
import com.example.zopera.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author -WKj-
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2023-04-07 01:28:39
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {
    @Resource
    private RoleMenuMapper roleMenuMapper;

    /**
     * @param roleId
     * @param menuIds
     * @Transactional : 失误注解
     */
    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
//        先删除所有绑定
        roleMenuMapper.deleteByRoleId(roleId);
//        再添加绑定
        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}




