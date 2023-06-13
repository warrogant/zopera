package com.example.zopera.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zopera.common.Result;
import com.example.zopera.entity.*;
import com.example.zopera.mapper.*;
import com.example.zopera.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/reception")
public class ReceptionController {

    @Resource
    private ReceptionMapper receptionMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private ActivityService activityService;

    @Resource
    private FileMapper fileMapper;
    @Resource
    private FavoriteMapper favoriteMapper;

    /**
     *
     * 根据戏曲类型查询数据
     * 
     * @param operaType
     * @return
     */
    @GetMapping("/operatype/{operaType}")
    public List<Opera> SelectOperaType(@PathVariable String operaType) {
        // System.out.println("operaType:"+operaType);
        return receptionMapper.SelectOperaType(operaType);
    }

    /**
     * 查询前四条的戏曲数据信息
     * 
     * @return
     */
    @GetMapping("/operalimit")
    public List<Opera> SelectOperaLimit() {
        return receptionMapper.SelectOperaLimit();
    }

    /**
     *
     * 根据戏曲类型查询数据
     * 
     * @param operaType
     * @return
     */
    @GetMapping("/operaCreateTime/{operaType}")
    public List<Opera> SelectOperaCreateTime(@PathVariable String operaType) {
        System.out.println("operaType:" + operaType);
        return receptionMapper.SelectOperaCreateTime(operaType);
    }

    /**
     * 根据创建时间进行降序查询前6条的戏曲数据信息
     * 
     * @return
     */
    @GetMapping("/operaCreateTimeLimit")
    public List<Opera> SelectOperaCreateTimeLimit() {
        return receptionMapper.SelectOperaCreateTimeLimit();
    }

    /**
     * 查询前8条的戏曲数据信息
     *
     * @return
     */
    @GetMapping("/SelectOperaLimit8")
    public List<Opera> SelectOperaLimit8() {
        return receptionMapper.SelectOperaLimit8();
    }

    /**
     * 查询所有的戏曲数据
     */
    @GetMapping("/findFavoriteOperaPage")
    public Result findFavoriteOperaPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
        @RequestParam(defaultValue = "") String operaType, @RequestParam Integer state,
                                        @RequestParam(defaultValue = "") String name) {
        IPage<Opera> objectPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Opera> queryWrapper = new QueryWrapper<>();
        IPage<Opera> operaIPage;
        switch (state) {
            case 0:
                queryWrapper.eq("state", 0);
                if (!"".equals(name)) {
                    queryWrapper.like("name", name);
                }
                queryWrapper.eq("is_delete", false); // 查询未删除的记录
                operaIPage = fileMapper.selectPage(objectPage, queryWrapper);
                break;
            case 1:
                queryWrapper.eq("state", 1);
                if (!"".equals(name)) {
                    queryWrapper.like("name", name);
                }
                if (!"".equals(operaType)) {
                    queryWrapper.like("opera_type", operaType);
                }
                queryWrapper.eq("is_delete", false); // 查询未删除的记录
                operaIPage = fileMapper.selectPage(objectPage, queryWrapper);
                break;
            case 2:
                queryWrapper.eq("state", 2);
                queryWrapper.eq("is_delete", false); // 查询未删除的记录
                operaIPage = fileMapper.selectPage(objectPage, queryWrapper);
                break;
            default:
                if (!"".equals(operaType)) {
                    queryWrapper.like("opera_type", operaType);
                }
                if (!"".equals(name)) {
                    queryWrapper.like("name", name);
                }
                queryWrapper.eq("is_delete", false); // 查询未删除的记录
                operaIPage = fileMapper.selectPage(objectPage, queryWrapper);
        break;
        }
        return Result.success(operaIPage);
    }

    /**
     * 根据视频类型查询戏曲数据信息
     * 
     * @return
     */
    @GetMapping("/operaVideoType/{operaType}")
    public List<Opera> SelectVideoType(@PathVariable String operaType) {
        return receptionMapper.SelectVideoType(operaType);
    }

    /**
     * ID查询戏曲视频数据
     */
    @GetMapping("/operavideo/{id}")
    public Result findOperaById(@PathVariable Integer id) {
        return Result.success(fileMapper.selectById(id));
    }

    /**
     * 根据视频播放量查询戏曲前10的数据信息
     * 
     * @return
     */
    @GetMapping("/operaVideoList")
    public List<Opera> SelectOperaList() {
        List<Opera> operas = receptionMapper.SelectOperaList();
        return operas;
    }

    /**
     * 活动信息
     * 
     * @return
     */
    @GetMapping("/operaActivityAll")
    public List<Activity> ActivityAll() {
        return activityMapper.selectList(null);
    }

    /**
     * ID查询活动数据
     */
    @GetMapping("/operaActivity/{id}")
    public Result findActivityById(@PathVariable Integer id) {
        return Result.success(activityMapper.selectById(id));
    }

    // 新增活动信息
    @PostMapping("/ActivitySave")
    private Result ActivitySave(@RequestBody Activity activity) {
        return Result.success(activityService.saveOrUpdate(activity));
    }

    /**
     * 更新视频播放量
     *
     * @param id
     * @return
     */
    @GetMapping("/updateOperaVideoByIdCount/{id}")
    public Result updateOperaVideoByIdCount(@PathVariable Integer id) {
        int idCount = fileMapper.updateOperaByIdPlayback(id);
        Opera opera = new Opera();
        opera.setPlayback(idCount);
        return Result.success(idCount);
    }

    @Resource
    private CultureMapper cultureMapper;

    @GetMapping("/cultureFindAll")
    public Result culture() {
        return Result.success(cultureMapper.selectList(null));
    }

    /**
     * 根据用户Id查询用户的戏曲收藏
     * 
     * @param id
     * @return
     */
    @GetMapping("/findFavoriteOperaById/{id}")
    public Result findFavoriteOperaById(@PathVariable Integer id) {
        return Result.success(receptionMapper.findFavoriteOperaById(id));
    }

    /**
     * 根据上传者查询用户收藏数据
     * 
     * @param uploader
     * @return
     */
    @GetMapping("/findOperaUploaderPage")
    public Result findOperaUploaderPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
        @RequestParam(defaultValue = "") String uploader) {
        IPage<Opera> objectPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Opera> queryWrapper = new QueryWrapper<>();
        if (!"".equals(uploader)) {
            queryWrapper.like("uploader", uploader);
        }
        queryWrapper.eq("is_delete", false);// 查询未删除的记录
        IPage<Opera> operaPage = fileMapper.selectPage(objectPage, queryWrapper);
        return Result.success(operaPage);
        // queryWrapper.orderByDesc("id");
    }

    @GetMapping("/findActivityPage")
    public Result findActivityPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
        @RequestParam Integer state, @RequestParam(defaultValue = "") String name,
        @RequestParam(defaultValue = "") String sponsor) {
        IPage<Activity> objectPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        IPage<Activity> activityPage;
        switch (state) {
            case 0:
                queryWrapper.eq("state", 0);
                if (!"".equals(name)) {
                    queryWrapper.like("name", name);
                }
                activityPage = activityMapper.selectPage(objectPage, queryWrapper);
                break;
            case 1:
                queryWrapper.eq("state", 1);
                queryWrapper.orderByDesc("end_time");
                activityPage = activityMapper.selectPage(objectPage, queryWrapper);
                break;
            case 2:
                queryWrapper.eq("state", 2);
                activityPage = activityMapper.selectPage(objectPage, queryWrapper);
                break;
            default:
                if (!"".equals(name)) {
                    queryWrapper.like("name", name);
                }
                if (!"".equals(sponsor)) {
                    queryWrapper.like("sponsor", sponsor);
                }
                activityPage = activityMapper.selectPage(objectPage, queryWrapper);
                break;
        }
        return Result.success(activityPage);
    }

    /**
     * 更新活动审核状态
     *
     * @param id, state, stateInfo
     * @return
     */
    @PostMapping("/updateActivityByIdState")
    public Result updateActivityByIdState(@RequestParam Integer id,
                                          @RequestParam Integer state,
        @RequestParam(defaultValue = "") String stateInfo) {
        int updateState = receptionMapper.updateActivityByIdState(id, state, stateInfo);
        return Result.success(updateState);
    }

    /**
     * 根据戏曲类型查询数据
     * 
     * @param operaType
     * @return
     */
    @GetMapping("/selectOperaCulture/{operaType}")
    public Result selectOperaCulture(@PathVariable String operaType) {
        return Result.success(receptionMapper.SelectOperaCulture(operaType));
    }

    /**
     * 更新戏曲文化点击量
     *
     * @param operaType
     * @return
     */
    @PostMapping("/updateOperaCultureByIdHits/{operaType}")
    public Result updateCultureByIdHits(@PathVariable String operaType) {
        int idHits = receptionMapper.updateCultureByIdHits(operaType);
        Culture culture = new Culture();
        culture.setHits(idHits);
        return Result.success(idHits);
    }

    /**
     * 活动批量删除
     * @param ids
     * @return
     */

    @PostMapping("/del/batch")
    public Result deleteActivityBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Activity> files = activityMapper.selectList(queryWrapper);
        for (Activity file : files) {
//            file.setIsDelete(true);
            activityMapper.deleteById(file);
        }
        return Result.success();
    }
}
