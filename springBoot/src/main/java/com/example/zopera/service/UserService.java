package com.example.zopera.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zopera.controller.dto.UserPasswordDTO;
import com.example.zopera.entity.User;
import com.example.zopera.controller.dto.UserDto;

/*
业务
 */
public interface UserService extends IService<User> {
    UserDto login(UserDto userDto);

    User register(UserDto userDto);

    void updatePassword(UserPasswordDTO userPasswordDTO);
}
