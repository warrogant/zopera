package com.example.zopera.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.example.zopera.common.Result;
import com.example.zopera.entity.Activity;
import com.example.zopera.entity.Opera;
import com.example.zopera.entity.User;
import com.example.zopera.mapper.FileMapper;
import com.example.zopera.service.ActivityService;
import com.example.zopera.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @Resource
    private FileMapper fileMapper;

    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    @GetMapping("/countUser")
    public Result countUser() {
        return Result.success(fileMapper.countUser());
    }
    @GetMapping("/countOpera")
    public Result countOpera() {
        return Result.success(fileMapper.countOpera());
    }
    @GetMapping("/sumPlayback")
    public Result sumPlayback() {
        return Result.success(fileMapper.sumPlayback());
    }
    @GetMapping("/countActivity")
    public Result countActivity() {
        return Result.success(fileMapper.countActivity());
    }


    /**
     * 按季度统计用户注册
     * 
     * @return
     */
    @GetMapping("/members")
    public Result members() {
        List<User> list = userService.list();
        int q1 = 0; // 第一季度
        int q2 = 0; // 第二季度
        int q3 = 0; // 第三季度
        int q4 = 0; // 第四季度
        for (User user : list) {
            Date createTime = user.getCreatetime();
            // 获得指定日期所属季度
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1:
                    q1 += 1;
                    break;
                case Q2:
                    q2 += 1;
                    break;
                case Q3:
                    q3 += 1;
                    break;
                case Q4:
                    q4 += 1;
                    break;
                default:
                    break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    /**
     * 查询每种戏曲类型的戏曲总数
     */
    @GetMapping("/SelectOperaTypeCount")
    public Result SelectOperaTypeCount() {
        List<Opera> operas = fileMapper.SelectOperaTypeCount();
        return Result.success(operas);
    }

    /**
     * 查询每种戏曲类型的戏曲播放量
     */
    @GetMapping("/SelectOperaTypePlayback")
    public Result SelectOperaTypePlayback() {
        List<Opera> operas = fileMapper.SelectOperaTypePlayback();
        return Result.success(operas);
    }

    /**
     * 统计消趣
     * 
     * @return
     */
    @GetMapping("/EventRelease")
    public Result EventRelease() {
        List<Activity> list = activityService.list();
        int q1 = 0; // 第一季度
        int q2 = 0; // 第二季度
        int q3 = 0; // 第三季度
        int q4 = 0; // 第四季度
        for (Activity activity : list) {
            Date publishTime = activity.getPublishTime();
            // 获套指定日期所展季差
            Quarter quarter = DateUtil.quarterEnum(publishTime);
            switch (quarter) {
                case Q1:
                    q1 += 1;
                    break;
                case Q2:
                    q2 += 1;
                    break;
                case Q3:
                    q3 += 1;
                    break;
                case Q4:
                    q4 += 1;
                    break;
                default:
                    break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }
}
