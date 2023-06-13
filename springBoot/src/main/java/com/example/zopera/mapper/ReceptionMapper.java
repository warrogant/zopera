package com.example.zopera.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zopera.entity.Comment;
import com.example.zopera.entity.Culture;
import com.example.zopera.entity.Notice;
import com.example.zopera.entity.Opera;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ReceptionMapper extends BaseMapper<Opera> {

    // 前台首页数据访问
    @Select("SELECT * FROM notice ORDER BY create_time DESC LIMIT 5;")
    List<Notice> SelectNoticeLimit();

    /**
     * 根据类型查询所有的戏曲
     * 
     * @param operaType
     * @return
     */
    @Select("SELECT id,name,url,playback,pic,introduce,create_time FROM opera WHERE opera_type = #{operaType} and is_delete != 1 and state = 1 LIMIT 4;")
    List<Opera> SelectOperaType(String operaType);

    @Select("SELECT id,name,url,playback,pic,introduce,create_time FROM opera  WHERE is_delete != 1 and state = 1 ORDER BY playback DESC LIMIT 4;")
    List<Opera> SelectOperaLimit();

    @Select("SELECT id,name,url,playback,pic,introduce,create_time FROM opera WHERE opera_type = #{operaType} and is_delete != 1 and state = 1 ORDER BY create_time DESC LIMIT 6;")
    List<Opera> SelectOperaCreateTime(String operaType);

    @Select("SELECT id,name,url,playback,pic,introduce,create_time FROM opera WHERE is_delete != 1 and state = 1 ORDER BY create_time DESC LIMIT 6;")
    List<Opera> SelectOperaCreateTimeLimit();

    // 前台戏曲页面
//    @Select("SELECT id,name,url,playback,pic,introduce,create_time FROM opera WHERE is_delete != 1 ;")
//    List<Opera> SelectVideoAll();

    @Select("SELECT * FROM opera WHERE opera_type = #{operaType} and is_delete != 1 and state = 1 ;")
    List<Opera> SelectVideoType(String operaType);

    // 前台排行榜页面
    @Select("SELECT * FROM opera WHERE is_delete != 1 and state = 1 ORDER BY playback DESC LIMIT 10;")
    List<Opera> SelectOperaList();

    // 前台戏曲文化页面戏曲视频数据
    @Select("SELECT * FROM opera WHERE is_delete != 1 and state = 1 LIMIT 8;")
    List<Opera> SelectOperaLimit8();


    /**
     * 根据公告Id查询点击量
     * 
     * @param id
     * @return
     */
    @Update("update notice set hits = hits + 1 WHERE id = #{id};")
    int updateNoticeByIdHits(Integer id);

    @Select("SELECT hits FROM notice WHERE id = #{id};")
    int findNoticeHitsById(Integer id);

    // 前台戏曲文化页面戏曲信息数据
    @Select("SELECT * FROM culture WHERE type = #{operaType};")
    List<Culture> SelectOperaCulture(String operaType);
    /**
     * 根据公告Id查询点击量
     *
     * @param operaType
     * @return
     */
    @Update("update culture set hits = hits + 1 WHERE type = #{operaType};")
    int updateCultureByIdHits(String operaType);

    /**
     * 查询用户的戏曲收藏
     * @param id
     * @return
     */
    @Select("SELECT * FROM opera where id in(SELECT opera_id FROM favorite WHERE user_id = #{id} ) and state = 1")
    List<Opera> findFavoriteOperaById(Integer id);

    /**
     * 查询用户的上传戏曲
     * @param uploader
     * @return
     */
//    @Select("SELECT * FROM opera where uploader = #{uploader}; ")
//    List<Opera> findOperaUploader(String uploader);

    /**
     * 查询用户发布的活动
     * @param id
     * @return
     */
//    @Select("SELECT * FROM activity where sponsor = #{sponsor} ;")
//    String findCultureSponsor(String sponsor);


//    @Update("UPDATE activity SET state = #{state} WHERE id = #{id};")
    int updateActivityByIdState(Integer id,Integer state,String stateInfo);


}
