package com.example.zopera.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zopera.entity.Role;

import java.util.List;

/**
 * @author -WKj-
 * @description 针对表【role】的数据库操作Service
 * @createDate 2023-04-07 01:28:39
 */
public interface RoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);


    List<Integer> getRoleMenu(Integer roleId);
}
