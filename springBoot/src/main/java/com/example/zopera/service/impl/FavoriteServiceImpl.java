package com.example.zopera.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zopera.common.Result;
import com.example.zopera.common.ResultConstants;
import com.example.zopera.entity.Favorite;
import com.example.zopera.service.FavoriteService;
import com.example.zopera.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author -WKj-
 * @description 针对表【favorite】的数据库操作Service实现
 * @createDate 2023-05-04 15:06:34
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    @Override
    public Result selectFavorite(Integer userId, Integer operaId) {
        if (userId != null && operaId != null) {
            return Result.success(favoriteMapper.findByUserIdAndOperaId(userId, operaId));
        }
        return Result.error(ResultConstants.CODE_500,"没有东西");
    }

    /***
     * 添加收藏
     * @param userId
     * @param operaId
     * @return
     */
    @Override
    public boolean addFavorite(Integer userId, Integer operaId) {
        // 判断是否已经收藏
        if (favoriteMapper
            .selectCount(new QueryWrapper<Favorite>()
                    .eq("user_id", userId)
                    .eq("opera_id", operaId)) > 0) {
            return true; // 已经收藏，直接返回true
        } else {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setOperaId(operaId);
            return favoriteMapper.insert(favorite) > 0;
        }
    }

    /**
     * 删除收藏
     * @param userId
     * @param operaId
     * @return
     */
    @Override
    public boolean deleteFavorite(Integer userId, Integer operaId) {
        return favoriteMapper.delete(new QueryWrapper<Favorite>()
                .eq("user_id", userId)
                .eq("opera_id", operaId)) > 0;
    }

    /**
     * 查询用户收藏
     * @param userId
     * @return
     */
    @Override
    public List<Favorite> selectByUserId(Integer userId) {
        return favoriteMapper.selectByUserId(userId);
    }
}
