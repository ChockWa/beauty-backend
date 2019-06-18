package com.chockwa.beauty.dto;

import com.chockwa.beauty.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 17:47
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto extends User {
    private String verifyCode;
    private String confirmPassword;
    private String uuid;
}
