package com.system.dms.service;

import com.system.dms.entity.Article;

import java.util.List;

public interface ArticleService {
    long addArticle(Article article);
    boolean deleteArticle(long articleId);
    boolean updateArticle(Article article);
    Article getArticle(long articleId);
    List<Article> getAllList();
}
