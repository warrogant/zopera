package com.example.zopera.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zopera.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author -WKj-
 * @description 针对表【role_menu】的数据库操作Mapper
 * @createDate 2023-04-08 00:25:06
 * @Entity com.example.zopera.entity.RoleMenu
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    @Delete("delete from role_menu where role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    @Select("select menu_id from role_menu where role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}




