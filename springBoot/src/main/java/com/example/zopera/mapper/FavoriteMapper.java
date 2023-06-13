package com.example.zopera.mapper;

import com.example.zopera.entity.Favorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * @author -WKj-
 * @description 针对表【favorite】的数据库操作Mapper
 * @createDate 2023-05-04 15:06:34
 * @Entity com.example.zopera.entity.Favorite
 */
public interface FavoriteMapper extends BaseMapper<Favorite> {
//    // 根据用户ID查询收藏记录列表
    @Select("select * from favorite where user_id = #{userId}")
    List<Favorite> selectByUserId(Integer userId);
//    // 根据用户ID和戏曲ID查询收藏记录
    @Select("select * from favorite where user_id = #{userId} and opera_id = #{operaId}")
    List<Favorite> findByUserIdAndOperaId(Integer userId, Integer operaId);
}
