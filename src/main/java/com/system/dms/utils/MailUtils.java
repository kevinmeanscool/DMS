package com.system.dms.utils;

import com.system.dms.entity.Email;

import java.util.List;

public class MailUtils {
    public static void sendMail(String toemial,String cc,String subject,String content,List<String> attachments) throws Exception{
        String userName = "619220726@qq.com"; // 发件人邮箱
        String password = "lywrzpnegtaxbcgh"; // 发件人密码
        String smtpHost = "smtp.qq.com"; // 邮件服务器

        //toemial = "2476177663@qq.com"; // 收件人，多个收件人以半角逗号分隔
        //cc = "3261045722@qq.com"; // 抄送，多个抄送以半角逗号分隔
        //subject = "AI机器人发送的邮件"; // 主题
        //content = "你好，年糕！"; // 正文，可以用html格式的哟
        //attachments = null; // 附件的路径，多个附件也不怕

        //新建邮件
        Email email = Email.entity(smtpHost, userName, password, toemial, cc, subject, content, attachments);
        email.send(); // 发送！
    }
}
