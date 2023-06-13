package com.example.zopera.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zopera.entity.Menu;
import com.example.zopera.mapper.MenuMapper;
import com.example.zopera.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author -WKj-
 * @description 针对表【menu】的数据库操作Service实现
 * @createDate 2023-04-07 02:34:47
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Override
    public List<Menu> findMenu(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.like("name", name);
//        queryWrapper.orderByDesc("id");
//        查询所有数据
        List<Menu> list = list(queryWrapper);
//        pid为null的一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
//        遍历查询二级菜单
        for (Menu menu : parentNodes) {
//            筛选 menu.getId().equals(m.getPid())
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}




