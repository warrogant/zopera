package com.example.zopera.mapper;

import com.example.zopera.entity.Activity;
import com.example.zopera.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author -WKj-
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2023-04-28 15:17:32
* @Entity com.example.zopera.entity.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT * FROM comment WHERE foreign_id = #{foreignId};")
    List<Comment> findAllByForeignId(Integer foreignId);

    @Select("SELECT * FROM comment WHERE foreign_type = #{foreignType};")
    List<Comment> findAllByForeignType(Integer foreignType);

}




