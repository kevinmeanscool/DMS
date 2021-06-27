package com.system.dms.controller;

import com.system.dms.entity.Article;
import com.system.dms.entity.Project;
import com.system.dms.entity.User;
import com.system.dms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class HomePageController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DMSService dmsService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MissionService missionService;


    @RequestMapping(value = "/toHomePage")
    public String toHomePage(HttpServletRequest request){
        //系统设置
        HttpSession session = request.getSession();
        session.setAttribute("DMSInfo",dmsService.getBasicInfoOfSystem());
        //准备主页内容
        return "HomePage";
    }

    @RequestMapping(value = "/toHelpDocument")
    public String toHelpDocument(Model model){
        List<Article> articleList = articleService.getAllList();
        model.addAttribute("articleList",articleList);
        return "HelpDocumentPage";
    }

    @RequestMapping(value = "/toBackManage")
    public String toBackManage(Model model){
        //帮助文档
        List<Article> articleList = articleService.getAllList();
        //用户
        List<User> userList = userService.getAllUserInfo();

        model.addAttribute("articleList",articleList);
        model.addAttribute("userList",userList);

        return "BackManage";
    }

    @RequestMapping(value = "/toForgetPasswordPage")
    public String toForgetPassword(){return "ForgetPassword";}

    @RequestMapping(value = "/toForgetUsername")
    public String toForgetUsername(){return "ForgetUsername";};

    @RequestMapping(value = "/toBuildProjectPage")
    public String toBuildProject(){return "BuildProjectPage";}

    @RequestMapping(value = "/toManageProject")
    public String toProjectManage(HttpServletRequest request, Model model){
        //获取当前用户id
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");
        //根据id获取用户相关的项目(发起的+参与的)
        //作为发起者的项目
        List<Project> projectListAsBuilder = projectService.getProjectListByIdAsBuilder(user.getId());
        model.addAttribute("projectListAsBuilder",projectListAsBuilder);
        model.addAttribute("projectListAsBuilder_size",projectListAsBuilder.size());

        //作为参与者的项目
        List<Project> projectListAsParticipant = projectService.getProjectListByIdAsParticipant(user.getId());
        //查询未阅读消息的数量
        List<Integer> NotReadingMessageCounts = missionService.getCountOfMessageNotReading(user.getId(),projectListAsParticipant);

        model.addAttribute("projectListAsParticipant",projectListAsParticipant);
        model.addAttribute("projectListAsParticipant_size",projectListAsParticipant.size());
        model.addAttribute("NotReadingMessageCounts",NotReadingMessageCounts);

        return "ProjectManagePage";
    }

    private void print(List<Project> projects){
        for(Project project:projects)
            System.out.println(project.toString());
    }
}
