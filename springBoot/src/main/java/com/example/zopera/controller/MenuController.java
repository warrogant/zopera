package com.example.zopera.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zopera.common.Result;
import com.example.zopera.common.ResultConstants;
import com.example.zopera.entity.Dictionary;
import com.example.zopera.entity.Menu;
import com.example.zopera.mapper.DictionaryMapper;
import com.example.zopera.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Resource
    private DictionaryMapper dict;

    //    查询
    @GetMapping
    public Result findAllMenu(@RequestParam(defaultValue = "") String name) {
        return Result.success(menuService.findMenu(name));
    }

    //    新增和修改
    @PostMapping("/save")
    private Result saveMenu(@RequestBody Menu role) {

        return Result.success(menuService.saveOrUpdate(role));
    }

    //    删除
    @DeleteMapping("/del/{id}")
    public Result deleteMenu(@PathVariable("id") Integer id) {
        return Result.success(menuService.removeById(id));
    }

    //    批量删除
    @DeleteMapping("/del/batch")
    public Result batchDeleteMenu(@RequestBody List<Integer> ids) {

        return Result.success(menuService.removeByIds(ids));
    }

    //    查询
    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.success(menuService.list().stream().map(Menu::getId));
    }

    //    导航图标
    @GetMapping("/symbol")
    private Result getIcons() {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
//      查询类型为symbol的数据
        queryWrapper.eq("type", ResultConstants.DICT_TYPE_SYMBOL);
        return Result.success(dict.selectList(null));
    }
    //    分页查询
//    @GetMapping("/page")
//    public Result findPage(@RequestParam Integer pageNum,
//                           @RequestParam Integer pageSize,
//                           @RequestParam String name
//    ) {
//        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("name", name);
//        queryWrapper.orderByDesc("id");
//        return Result.success(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
//    }

}
