package com.example.zopera.controller.dto;

import com.example.zopera.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接收前端请求的参数
 */
@Data
public class UserDto {
    private Integer id;
    private String nickname;
    private String username;
    private String password;
    private String avator;  //头像
    private String token;
    private String role;
    private String icon;
    private String children;
    private List<Menu> menus;
}
