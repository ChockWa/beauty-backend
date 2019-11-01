package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @auther: zhuohuahe
 * @date: 2019/10/30 15:19
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailContent {
    private String receiver;
    private String title;
    private String content;
}
