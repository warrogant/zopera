package com.example.zopera.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zopera.controller.dto.UserPasswordDTO;
import com.example.zopera.entity.User;
import org.apache.ibatis.annotations.Update;

/**
 * User数据访问
 */
//@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);
}
