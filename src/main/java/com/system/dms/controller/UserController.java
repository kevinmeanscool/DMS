package com.system.dms.controller;

import com.system.dms.entity.AjaxResult;
import com.system.dms.entity.HeadImage;
import com.system.dms.entity.User;
import com.system.dms.service.UserService;
import com.system.dms.staticEnum.UserEnum;
import com.system.dms.staticEnum.UserIdentityEnum;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/toLogin")
    public String toLoginPage(){return "Login";}
    @RequestMapping(value = "/toRegister")
    public String toRegisterPage(){
        return "Register";
    }
    @RequestMapping(value = "/toPersonalCenter")
    public String toPersonalCenter(){return "PersonalCenter";}

    @RequestMapping(value = "/register")
    public String register(@ModelAttribute("user") User user,Model model){
        //判断注册数量是否超过6个
        if(userService.isAccountCountRight(user)){
            //用户信息初始化
            user.setPersonprofile(UserEnum.DEFAULTPERSONPROFILE.getInfo());
            if(user.getGender().equals("男"))
                user.setImageurl(UserEnum.DEFAULTIMAGEURL_MAN.getInfo());
            else
                user.setImageurl(UserEnum.DEFAULTIMAGEURL_WOMAN.getInfo());
            user.setIdentity(UserEnum.DEFAULTIDENTITY.getInfo());
            user.setNickname(UserEnum.DEFAULTNICKNAME_PRE.getInfo()+user.getUsername());
            user.setStatus(Long.valueOf(UserEnum.DEFAULTSTATUS.getInfo()));

            //进行注册
            boolean result = userService.register(user);

            if(result){
                return "OperationSuccessPage";
            }
            else{
                model.addAttribute("message","用户名已存在");
                return "Register";
            }
        }
        else {
            model.addAttribute("message","邮箱绑定账号已达到上限：6个，无法继续创建，如有遗忘请选择'忘了用户名'找回");
            return "Register";
        }
    }

    @RequestMapping(value = "/login")
    public String login(@ModelAttribute("user") User user,Model model,HttpServletRequest request){
        String result = userService.login(user);
        if (result.equals("success")){
            User userInfo = userService.getUserInfoByUsername(user.getUsername());
            userInfo.setPassword("");
            HttpSession session = request.getSession();
            session.setAttribute("userInfo",userInfo);
            model.addAttribute("message",userInfo.toString());
            return "HomePage";
        }
        else{
            model.addAttribute("message",result);
            return "Login";
        }
    }

    @RequestMapping(value = "/logout")
    public String logout(String username,HttpServletRequest request){
        //清除session中的userInfo
        HttpSession session = request.getSession();
        session.setAttribute("userInfo",null);
        //更改user的状态
        if(userService.logout(username).equals("success")){
            return "HomePage";
        }
        else {
            return "fail";
        }
    }

    @RequestMapping(value = "/uploadHeadImage")
    @ResponseBody
    public String uploadHeadImage(@RequestParam MultipartFile file, HttpServletRequest request){
        //MultipartFile转file
        String fileNameTemp = file.getOriginalFilename();
        String prefix = fileNameTemp.substring(fileNameTemp.lastIndexOf("."));
        File HeadImage = null;
        try {
            HeadImage = File.createTempFile("temp",prefix);
            file.transferTo(HeadImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(HeadImage.getName());


        HeadImage headImage = new HeadImage();
        headImage.setHeadImg(HeadImage);
        headImage.setHeadImgFileName(fileNameTemp);
        if(headImage.getHeadImg()!=null){
//            String headImgContantType = headImage.getHeadImgContantType();
            String headImgFileName = headImage.getHeadImgFileName();
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("userInfo");
            String rootpath = null;
            try {
                rootpath = ResourceUtils.getURL("classpath:").getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            System.out.println("rootpath:"+rootpath);
            String imagepath = StringUtils.substringBefore(rootpath,"out")+"static/images";
//            System.out.println("imagepath:"+imagepath);
            String imageoutpath = request.getServletContext().getRealPath("images");
//            System.out.println("imageoutpath:"+imageoutpath);

            //处理头像
            //1、保存头像到/image
            //获取保存路径的绝对地址(这里还是需要改一下)
            String filePath= imagepath;

            //生成带格式的文件名称
            String fileName= user.getId()+headImgFileName.substring(headImgFileName.lastIndexOf("."));

            try {
                //复制文件到web目录下持久化
                FileUtils.copyFile(headImage.getHeadImg(), new File(filePath,fileName));
//                System.out.println("本地："+filePath);
                //同时对服务器输出的image进行复制，便于前端实时响应
                FileUtils.copyFile(headImage.getHeadImg(), new File(imageoutpath,fileName));
//                System.out.println("服务器："+filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //把新的url存入对象
            user.setImageurl("/images/"+fileName);
            //把对应用户的ImageURL记录替换了
            boolean result = userService.uploadHeadImage(user);

            if(result){
                //更新session
                session.setAttribute("userInfo",user);
                //返回结果
                return "保存成功，请刷新页面";
            }
            else {
                return "保存失败，请重试！";
            }
        }
        else {
            return "error:图片路径为空";
        }
    }

    @RequestMapping(value = "/updateUserInfo")
    @ResponseBody
    public String updateUserInfoById(@RequestParam String ObjectUser,HttpServletRequest request){
        JSONObject userObject = JSONObject.fromObject(ObjectUser);
        User user = (User) userObject.toBean(userObject,User.class);
        if(userService.updateUserInfoById(user)){
            //更新session
            HttpSession session = request.getSession();
            User userInfo = (User) session.getAttribute("userInfo");
            userInfo.setNickname(user.getNickname());
            userInfo.setGender(user.getGender());
            userInfo.setPersonprofile(user.getPersonprofile());
            session.setAttribute("userInfo",userInfo);
            //返回结果
            return "保存成功";
        }
        else
            return "保存失败：I/O Exception";
    }
    @RequestMapping(value = "/changePassword")
    @ResponseBody
    public String changePassword(@RequestParam String currentPassword,@RequestParam String newPassword,HttpServletRequest request){
        //获取当前用户
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");
        //填充旧用户对象
        user.setPassword(currentPassword);
        //修改密码
        boolean result = userService.changePasswordById(user,newPassword);

        if(result)
            return "修改成功";
        else
            return "原密码错误，请重试";
    }

    @RequestMapping(value = "/changePasswordInForgerPassword")
    public String changePasswordInForgerPassword(@ModelAttribute("user") User user){
        boolean result = userService.changePasswordByUsername(user.getUsername(),user.getPassword());
        if(result)
            return "OperationSuccessPage";
        else
            return "fail";
    }

    @ResponseBody
    @RequestMapping(value = "/getUsernamesByEmail")
    public Object getUsernamesByEmail(@RequestParam String email){
        AjaxResult ajaxResult = new AjaxResult();
        List<String> users = userService.getUsernamesByEmail(email);
        JSONArray json = JSONArray.fromObject(users);
        ajaxResult.setMessage("ok");
        ajaxResult.setResultObj(json);
        return json;
    }

    @ResponseBody
    @RequestMapping(value = "/changeIdentity")
    public String changeIdentity(@RequestParam("userId") long userId,@RequestParam("operation") String operation){
        boolean result = false;

        if(operation.equals("up")){
            //赋予管理权限
            result = userService.changeIdentity(userId, UserIdentityEnum.MANAGER.ordinal());
        }
        else if(operation.equals("down")){
            //接触管理权限
            result = userService.changeIdentity(userId,UserIdentityEnum.GENERALUSER.ordinal());
        }

        return String.valueOf(result);
    }
}
