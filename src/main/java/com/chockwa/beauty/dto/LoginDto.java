package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 18:12
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginDto {

    private String userName;

    private String password;

    private String verifyCode;
}
