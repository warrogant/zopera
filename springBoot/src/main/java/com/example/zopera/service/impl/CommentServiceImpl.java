package com.example.zopera.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zopera.entity.Activity;
import com.example.zopera.entity.Comment;
import com.example.zopera.mapper.ActivityMapper;
import com.example.zopera.service.CommentService;
import com.example.zopera.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author -WKj-
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2023-04-28 15:17:32
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


}
