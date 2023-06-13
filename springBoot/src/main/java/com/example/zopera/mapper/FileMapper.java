package com.example.zopera.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zopera.entity.Opera;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface FileMapper extends BaseMapper<Opera> {
//    @Select("SELECT * FROM opera WHERE is_delete != 1")
//    List<Opera> selectAll();

//    @Select("SELECT playback FROM opera WHERE id = #{id};")
//    int findOperaPlaybackById(Integer id);
    @Update("update opera set playback = playback + 1 WHERE id = #{id};")
    int updateOperaByIdPlayback(Integer id);
    int updateOperaByIdState(Integer id,Integer state,String stateInfo);

    // 统计用户数据总数
    @Select("SELECT COUNT(*) FROM user")
    Integer countUser();
    // 统计戏曲数据总数
    @Select("SELECT COUNT(*) FROM opera")
    Integer countOpera();
    @Select("SELECT SUM(playback) FROM opera")
    Integer sumPlayback();
    // 统计活动数据总数
    @Select("SELECT COUNT(*) FROM activity")
    Integer countActivity();


    /**
     * 查询每种戏曲类型的戏曲总数
     * @return
     */
    @Select("SELECT opera_type,COUNT(*) AS total_count FROM opera GROUP BY opera_type;")
    List<Opera> SelectOperaTypeCount();

    /**
     * 据每种戏曲类型进行播放量统计
     * @return
     */
    @Select("SELECT opera_type, SUM(playback) AS total_playback FROM opera GROUP BY opera_type;")
    List<Opera> SelectOperaTypePlayback();



}
