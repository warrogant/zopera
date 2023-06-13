package com.example.zopera.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("opera")
public class Opera {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String uploader;
    private String name;
    private String operaType;
    private String pic;
//    播放量
    private long playback;
    private String url;
    private String introduce;
    private Boolean isDelete;
    private Boolean enable;
    private Date createTime;
    private int state;
    private String stateInfo;
    @TableField(exist = false)
    private Integer totalCount;
    @TableField(exist = false)
    private Integer totalPlayback;
    //    收藏状态
    @TableField(exist = false)
    private Boolean favoriteStatus;
}
