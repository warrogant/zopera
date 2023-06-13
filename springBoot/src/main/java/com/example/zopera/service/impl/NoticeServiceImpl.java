package com.example.zopera.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zopera.entity.Notice;
import com.example.zopera.service.NoticeService;
import com.example.zopera.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author -WKj-
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2023-05-07 02:45:09
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

}




