package com.example.zopera.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zopera.entity.Role;
import org.apache.ibatis.annotations.Select;

/**
 * @author -WKj-
 * @description 针对表【role】的数据库操作Mapper
 * @createDate 2023-04-07 01:28:39
 * @Entity com.example.zopera.entity.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from role where flag = #{flag}")
    Integer selectByFlag(String flag);
}




