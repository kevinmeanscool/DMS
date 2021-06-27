package com.system.dms.controller;

import com.system.dms.entity.Article;
import com.system.dms.entity.User;
import com.system.dms.service.ArticleService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/toEditArticle")
    public String toEditArticle(){
        return "ArticleEdit";
    }

    @RequestMapping(value = "/toUpdateArticle")
    public String toUpdateArticle(@RequestParam("articleId") long articleId, Model model){
        //获取文章对象
        Article article = articleService.getArticle(articleId);
        model.addAttribute("article_id",article.getId());
        model.addAttribute("article_title",article.getTitle());
        model.addAttribute("article_content",article.getContent());
        return "ArticleUpdate";
    }

    @ResponseBody
    @RequestMapping("/publishArticle")
    public long publishArticle(@RequestParam String ObjectArticle, HttpServletRequest request){
        JSONObject ArticleJSON = JSONObject.fromObject(ObjectArticle);
        Article article = (Article) ArticleJSON.toBean(ArticleJSON,Article.class);
        //填充操作者
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");
        article.setOperatorid(user.getId());

        long articleId = articleService.addArticle(article);

        return articleId;
    }

    @RequestMapping(value = "/getArticle")
    public String getArticle(@RequestParam("articleId") long articleId, Model model){
        //获取文章对象
        Article article = articleService.getArticle(articleId);
        //送往页面
        model.addAttribute("article",article);

        return "ArticlePage";
    }

    @ResponseBody
    @RequestMapping(value = "/updateArticle")
    public String updateArticle(@RequestParam String ObjectArticle){
        JSONObject ArticleJSON = JSONObject.fromObject(ObjectArticle);
        Article article = (Article) ArticleJSON.toBean(ArticleJSON,Article.class);

        if(articleService.updateArticle(article)){
            return String.valueOf(article.getId());
        }
        else
            return null;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteArticle")
    public String deleteArticle(@RequestParam("articleId") long articleId){
        if(articleService.deleteArticle(articleId)){
            return "ok";
        }
        else
            return "fail";
    }

}
