package com.system.dms.service.impl;

import com.system.dms.dao.ArticleMapper;
import com.system.dms.entity.Article;
import com.system.dms.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ArticleService")
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public long addArticle(Article article) {
        if(articleMapper.addArticle(article)){
            return article.getId();
        }
        else
            return -1;
    }

    @Override
    public boolean deleteArticle(long articleId) {
        return articleMapper.deleteArticle(articleId);
    }

    @Override
    public boolean updateArticle(Article article) {
        return articleMapper.updateArticle(article);
    }

    @Override
    public Article getArticle(long articleId) {
        return articleMapper.getArticle(articleId);
    }

    @Override
    public List<Article> getAllList() {
        return articleMapper.getAllList();
    }
}
