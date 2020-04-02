package com.kylin.upms.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMsg() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 邮件发送者，必须是真实的发送邮件的邮箱名称
        mailMessage.setFrom("1325373009@qq.com");
        // 邮件接受者@sina.cn
        mailMessage.setTo("1325373009@qq.com");
        // 邮件主题
        mailMessage.setSubject("重置密码");
        // 邮件内容
        mailMessage.setText("您的账户密码重置已完成，临时密码为：123456 " +
                " 临时密码有效期为三天，请您尽快修改密码！");
        // 发送邮件
        mailSender.send(mailMessage);
    }
}
