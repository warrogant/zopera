package com.example.zopera.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zopera.common.ResultConstants;
import com.example.zopera.controller.dto.UserDto;
import com.example.zopera.controller.dto.UserPasswordDTO;
import com.example.zopera.entity.Menu;
import com.example.zopera.entity.User;
import com.example.zopera.exception.ServiceException;
import com.example.zopera.mapper.RoleMapper;
import com.example.zopera.mapper.RoleMenuMapper;
import com.example.zopera.mapper.UserMapper;
import com.example.zopera.service.MenuService;
import com.example.zopera.service.UserService;
import com.example.zopera.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/*
 * 业务实现层
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private UserMapper userMapper;

    // 登录
    @Override
    public UserDto login(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one != null) {
            // 将userDto的参数复制一份给one
            BeanUtil.copyProperties(one, userDto, true);
            // 设置token
            String token = TokenUtils.createToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);

            // 获取当前角色菜单权限
            String role = one.getRole(); // ROLE_ADMIN
            // 设置用户菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDto.setMenus(roleMenus);
            return userDto;
        } else {
            // 抛出业务异常
            throw new ServiceException(ResultConstants.CODE_666, "用户名或密码错误");
        }
    }

    // 注册
    @Override
    public User register(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one == null) {
            one = new User();
            // 从userDto中复制参数到one
            BeanUtil.copyProperties(userDto, one, true);
            try {
                save(one);// 存进数据库中
            } catch (Exception e) {
                throw new ServiceException(ResultConstants.CODE_666, "用户已存在！");
            }
        } else {
            // 抛出业务异常
            throw new ServiceException(ResultConstants.CODE_666, "用户已存在");
        }
        return one;
    }

    /**
     * 修改密码
     * 
     * @param userPasswordDTO
     */
    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(ResultConstants.CODE_666, "修改失败");
        }
    }

    private User getUserInfo(UserDto userDto) {
        // 从数据库中获取用户参数与请求参数进行比较
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        // 避免脏数据
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(ResultConstants.CODE_500, "数据库中存在多条脏数据！");
        }
        return one;
    }

    // 获取当前菜单列表
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有的菜单集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        // 查询所有菜单
        List<Menu> menus = menuService.findMenu("");
        // new 一个最后筛选完成之后的list
        ArrayList<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色菜单
        for (Menu menu : menus) {
            // menuIds包含menu.getId()则加入菜单中
            // if (menuIds.contains(menu.getId())) {
            // roleMenus.add(menu);
            // }
            List<Menu> children = menu.getChildren();
            // 移除不在菜单列表中的子菜单
            boolean isSuccess = children.removeIf(child -> !menuIds.contains(child.getId()));
            // 子菜单不为空并且isSuccess为true
            if (children.size() != 0 && isSuccess) {
                roleMenus.add(menu);
            }
        }
        return menus;
    }

}
