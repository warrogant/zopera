package com.example.zopera.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zopera.common.Result;
import com.example.zopera.entity.Activity;
import com.example.zopera.entity.Comment;
import com.example.zopera.mapper.ActivityMapper;
import com.example.zopera.mapper.CommentMapper;
import com.example.zopera.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ActivityMapper activityMapper;

    /***
     *
     * @param content
     * @return
     */
    @PostMapping
    public Result addComment(@RequestBody Comment content) {
        return Result.success(commentMapper.insert(content));
    }

    @GetMapping("/findCommentAllForeign")
    public Map<String, Object> findCommentAllForeign(@RequestParam(defaultValue = "") Integer foreignType,
        @RequestParam Integer foreignId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        queryWrapper.eq("foreign_type", foreignType);
        queryWrapper.eq("foreign_id", foreignId);

        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<Comment> rootComments =
            comments.stream().filter(comment -> comment.getPid() == null).collect(Collectors.toList());
        for (Comment rootComment : rootComments) {
            rootComment.setChildren(comments.stream().filter(comment -> rootComment.getId().equals(comment.getPid()))
                .collect(Collectors.toList()));
        }
        map.put("comments", rootComments);
        return map;
    }

    /**
     * 查询不同模块的评论
     *
     * @param foreignId
     * @return
     */
    @GetMapping
    public Map<String, Object> getCommentsList(@RequestParam Integer foreignId) {
        Map<String, Object> map = new HashMap<>();
        List<Comment> comments = commentMapper.findAllByForeignId(foreignId);

        List<Comment> rootComments =
            comments.stream().filter(comment -> comment.getPid() == null).collect(Collectors.toList());
        for (Comment rootComment : rootComments) {
            rootComment.setChildren(comments.stream().filter(comment -> rootComment.getId().equals(comment.getPid()))
                .collect(Collectors.toList()));
        }
        map.put("comments", rootComments);
        return map;
    }

    /**
     * 根据id查询活动点击量
     * 
     * @param id
     */
    @GetMapping("/findActivityCountById/{id}")
    public Result findActivityCountById(@PathVariable Integer id) {
        int idCount = activityMapper.findActivityCountById(id);
        return Result.success(idCount);
    }

    /**
     * 更新点击量
     * 
     * @param id
     * @return
     */
    @GetMapping("/updateActivityByIdCount/{id}")
    public Result updateActivityByIdCount(@PathVariable Integer id) {
        int idCount = activityMapper.updateActivityByIdCount(id);
        Activity activity = new Activity();
        activity.setCount(idCount);
        return Result.success(idCount);
    }
    // public Map<String, Object> CommentAllForeign(Integer foreignType, Integer foreignId) {
    // QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
    // Map<String, Object> map = new HashMap<>();
    // queryWrapper.eq("foreign_type", foreignType);
    // queryWrapper.eq("foreign_id", foreignId);
    // List<Comment> comments = commentMapper.selectList(queryWrapper);
    // List<Comment> rootComments =
    // comments.stream().filter(comment -> comment.getPid() == null).collect(Collectors.toList());
    // for (Comment rootComment : rootComments) {
    // rootComment.setChildren(comments.stream().filter(comment -> rootComment.getId().equals(comment.getPid()))
    // .collect(Collectors.toList()));
    // }
    // map.put("comments", rootComments);
    // return map;
    // }
}
