package com.example.zopera.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zopera.entity.Menu;

import java.util.List;

/**
 * @author -WKj-
 * @description 针对表【menu】的数据库操作Service
 * @createDate 2023-04-07 02:34:47
 */
public interface MenuService extends IService<Menu> {

    List<Menu> findMenu(String name);
}
