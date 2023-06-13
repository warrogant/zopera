package com.example.zopera.mapper;

import com.example.zopera.entity.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author -WKj-
* @description 针对表【activity】的数据库操作Mapper
* @createDate 2023-04-27 14:43:19
* @Entity com.example.zopera.entity.Activity
*/
public interface ActivityMapper extends BaseMapper<Activity> {

    @Select("SELECT count FROM activity WHERE id = #{id};")
    int findActivityCountById(Integer id);
    @Update("update activity set count = count + 1 WHERE id = #{id};")
    int updateActivityByIdCount(Integer id);

}




