package com.example.zopera.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zopera.entity.Activity;
import com.example.zopera.mapper.NoticeMapper;
import com.example.zopera.mapper.ReceptionMapper;
import com.example.zopera.utils.TokenUtils;
import com.example.zopera.common.Result;
import com.example.zopera.entity.Notice;
import com.example.zopera.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService  noticeService;

    @Resource
    private ReceptionMapper receptionMapper;
    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 根据创建时间查询前5条数据
     * @return
     */
    @GetMapping("/noticeLimit")
    public Result noticeLimit() {
        return Result.success(receptionMapper.SelectNoticeLimit());
    }

    // 查询
    @GetMapping
    public Result findAllNotice() {
        return Result.success(noticeService.list());
    }

    /**
     * 根据Id查询公告信息
     * @return
     */
    @GetMapping("/findAllByNoticeId/{id}")
    public Result findAllByNoticeId(@PathVariable("id") Integer Id) {
        return Result.success(noticeMapper.selectById(Id));
    }

    /**
     * 根据Id查询公告信息
     * @return
     */
    @GetMapping("/findNoticeHitsById/{id}")
    public Result findNoticeHitsById(@PathVariable("id") Integer Id) {
        return Result.success(receptionMapper.findNoticeHitsById(Id));
    }
    /**
     * 更新点击量
     *
     * @param id
     * @return
     */
    @PostMapping("/updateNoticeByIdHits/{id}")
    public Result updateNoticeByIdHits(@PathVariable Integer id) {
        int idHits = receptionMapper.updateNoticeByIdHits(id);
        Notice notice = new Notice();
        notice.setHits(idHits);
        return Result.success(idHits);
    }


    // 新增和修改
    @PostMapping("/save")
    private Result saveNotice(@RequestBody Notice notice) {
        return Result.success(noticeService.saveOrUpdate(notice));
    }

    // 删除
    @DeleteMapping("/del/{id}")
    public Result deleteNotice(@PathVariable("id") Integer id) {
        return Result.success(noticeService.removeById(id));
    }

    // 批量删除
    @DeleteMapping("/del/batch")
    public Result batchDeleteNotice(@RequestBody List<Integer> ids) {

        return Result.success(noticeService.removeByIds(ids));
    }

    // 分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String picTitle) {
        IPage<Notice> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        if (!"".equals(picTitle)) {
            queryWrapper.like("pic_title", picTitle);
        }

        return Result.success(noticeService.page(page, queryWrapper));
    }

}
