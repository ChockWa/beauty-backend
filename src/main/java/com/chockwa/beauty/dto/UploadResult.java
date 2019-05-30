package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @auther: zhuohuahe
 * @date: 2019/5/30 16:29
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadResult {

    // {"url":"http://198.252.105.138:8080/group1/20190530/642672/e59e5a5c22044e0a96d51a74e78695e0.jpg","md5":"b07c96a29b4e068ec52074ec9690b993","path":"/group1/20190530/642672/e59e5a5c22044e0a96d51a74e78695e0.jpg","domain":"http://198.252.105.138:8080","scene":"image","scenes":"image","retmsg":"","retcode":0,"src":"/group1/20190530/642672/e59e5a5c22044e0a96d51a74e78695e0.jpg"}
    private String md5;
    private String url;
    private String src;
    private String path;
}
