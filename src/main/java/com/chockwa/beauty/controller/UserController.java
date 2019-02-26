package com.chockwa.beauty.controller;

import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * @auther: zhuohuahe
 * @date: 2019/2/26 19:47
 * @description:
 */
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getUser")
    public User getUser(){
        return userMapper.selectById(1L);
    }

    @GetMapping("upload")
    public void upload(){
        String serverUrl = "https://sm.ms/api/upload";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setConnection("Keep-Alive");
//        headers.setCacheControl("no-cache");
        String fileLocal = "F:\\019.jpg";
        FileSystemResource resource = new FileSystemResource(new File(fileLocal));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("smfile", resource);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(serverUrl, param, String.class);
        System.out.println(responseEntity);
        System.out.println(responseEntity.getBody());
    }
}
