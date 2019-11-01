package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chockwa.beauty.dto.MailContent;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther: zhuohuahe
 * @date: 2019/10/30 15:18
 * @description:
 */
@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserMapper userMapper;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${dns.web-site}")
    private String webSite;

    public void send(MailContent content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(content.getReceiver());
            message.setSubject(content.getTitle());
            message.setText(content.getContent());
            mailSender.send(message);
        } catch (Exception e) {
            log.error("郵件發送失敗:{}",content.getReceiver(),e);
            e.printStackTrace();
        }
    }

    public void sendBeautySiteMessageBatch(){
        List<User> users = userMapper.selectList(new QueryWrapper<User>().lambda().isNotNull(User::getEmail));
        List<String> receivers = users.stream().filter(e -> e.getEmail().contains("@")).map(User::getEmail).collect(Collectors.toList());
        receivers.add("chockwa@outlook.com");
        receivers.forEach(e -> {
            MailContent content = new MailContent();
            content.setReceiver(e);
            content.setTitle("广州深圳楼凤信息有更新");
            content.setContent("广州深圳楼凤有更新，並加入了桑拿信息模塊，欢迎各位老狼品尝。-> "+ webSite);
            send(content);
        });
    }
}
