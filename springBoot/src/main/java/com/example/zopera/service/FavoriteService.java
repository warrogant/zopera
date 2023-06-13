package com.example.zopera.service;

import com.example.zopera.common.Result;
import com.example.zopera.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author -WKj-
* @description 针对表【favorite】的数据库操作Service
* @createDate 2023-05-04 15:06:34
*/
public interface FavoriteService extends IService<Favorite> {
    Result selectFavorite(Integer userId, Integer operaId);
    boolean addFavorite(Integer userId, Integer operaId);
    boolean deleteFavorite(Integer userId, Integer operaId);
    List<Favorite> selectByUserId(Integer userId);

}
