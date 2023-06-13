package com.example.zopera.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zopera.common.Result;
import com.example.zopera.entity.Role;
import com.example.zopera.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * 权限接口
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    //    查询
    @GetMapping
    public Result findAllRole() {
        return Result.success(roleService.list());
    }

    //    新增和修改
    @PostMapping("/save")
    private Result saveRole(@RequestBody Role role) {

        return Result.success(roleService.saveOrUpdate(role));
    }

    //    删除
    @DeleteMapping("/del/{id}")
    public Result deleteRole(@PathVariable("id") Integer id) {
        return Result.success(roleService.removeById(id));
    }

    //    批量删除
    @DeleteMapping("/del/batch")
    public Result batchDeleteRole(@RequestBody List<Integer> ids) {

        return Result.success(roleService.removeByIds(ids));
    }

    //    分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String name
    ) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
//        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 绑定角色和菜单
     */
    @PostMapping("/roleMenu/{roleId}")
    private Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

    @GetMapping("/getRoleMenu/{roleId}")
    private Result getroleMenu(@PathVariable Integer roleId) {
        return Result.success(roleService.getRoleMenu(roleId));
    }
}
