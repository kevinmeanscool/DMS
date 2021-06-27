package com.system.dms.dao;

import com.system.dms.entity.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert("insert into Article value (id,#{title},#{content},current_timestamp,#{operatorid})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    boolean addArticle(Article article);

    @Delete("delete from Article where id = #{articleId}")
    boolean deleteArticle(@Param("articleId") long articleId);

    @Update("update Article set title = #{title},content = #{content} where id = #{id}")
    boolean updateArticle(Article article);

    @Select("select * from Article where id = #{articleId}")
    Article getArticle(@Param("articleId")long articleId);

    @Select("select * from Article")
    List<Article> getAllList();
}
