package com.example.zopera.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

/**
 * user表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String nickname;
    private String username;
    private String password;
    private String phone;
    private String email;
    private LocalDate birth;
    private String location;
    private String avator;      //头像
    //    @JsonIgnore
    private Date createtime;
    private String updatetime;
    private String role;
}
