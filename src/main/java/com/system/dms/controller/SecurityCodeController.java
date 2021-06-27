package com.system.dms.controller;

import com.system.dms.entity.Securitycode;
import com.system.dms.service.SecurityCodeService;
import com.system.dms.service.UserService;
import com.system.dms.utils.MailUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
@RequestMapping(value = "/SecurityCode")
public class SecurityCodeController {

    @Autowired
    private SecurityCodeService securityCodeService;

    @ResponseBody
    @RequestMapping("/sendSecurityCodeToEmail")
    /**
     * 该方法调用JavaMail包的方法，向邮箱发送邮箱验证码
     */
    public Object sendSecurityCodeToEmail(@RequestParam String ObjectSecurityCode){

        JSONObject securityCodeObject = JSONObject.fromObject(ObjectSecurityCode);
        Securitycode securitycode = (Securitycode) securityCodeObject.toBean(securityCodeObject,Securitycode.class);
        //生成4位验证码
        long scode = createSecurityCode(4);
        securitycode.setSecuritycode(scode);
        //尝试发送邮件
        try {
            MailUtils.sendMail(securitycode.getEmail(),"","缺陷管理系统验证码","您的验证码为："+scode+",如果非本人操作请忽略该邮件，切勿告知他人。(5分钟后验证码失效)",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //添加验证码注册记录
        boolean result;
        result = securityCodeService.addRecordOfSecurityCode(securitycode);
        //返回当前验证码
        if (result){
            return String.valueOf(scode);
        }
        else {
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sendSecurityCodeToEmailInForgetPassword")
    public String sendSecurityCodeToEmailInForgetPassword(@RequestParam String username){
        //验证用户名是否存在
        if(securityCodeService.isUserNameExisting(username)){
            Securitycode securitycode = securityCodeService.getSecuritycodeObjectByUsername(username);
            //生成4位验证码
            long scode = createSecurityCode(4);
            securitycode.setSecuritycode(scode);
            //尝试发送邮件
            try {
                MailUtils.sendMail(securitycode.getEmail(),"","缺陷管理系统验证码","您的验证码为："+scode+",如果非本人操作请忽略该邮件，切勿告知他人。(5分钟后验证码失效)",null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //添加验证码注册记录
            boolean result;
            result = securityCodeService.addRecordOfSecurityCode(securitycode);
            return String.valueOf(scode);
        }
        else {
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sendSecurityCodeToEmailInForgetUsername")
    public String sendSecurityCodeToEmailInForgetUsername(@RequestParam String email){
        //验证邮箱是否存在
        if(securityCodeService.isEmailExisting(email)){
            //生成4位验证码
            long scode = createSecurityCode(4);
            //尝试发送邮件
            try {
                MailUtils.sendMail(email,"","缺陷管理系统验证码","您的验证码为："+scode+",如果非本人操作请忽略该邮件，切勿告知他人。(5分钟后验证码失效)",null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return String.valueOf(scode);
        }else {
            return "fail";
        }
    }

    private long createSecurityCode(int bit){
        //生成一个4位的验证码
        String str="123456789";
        StringBuilder sb=new StringBuilder(bit);
        for(int i=0;i<bit;i++)
        {
            char ch=str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        long vcode = Long.valueOf(sb.toString());
        return vcode;
    }
}
