package com.example.zopera.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zopera.entity.Activity;
import com.example.zopera.service.ActivityService;
import com.example.zopera.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author -WKj-
* @description 针对表【activity】的数据库操作Service实现
* @createDate 2023-04-27 14:43:19
*/
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
    implements ActivityService{

}




