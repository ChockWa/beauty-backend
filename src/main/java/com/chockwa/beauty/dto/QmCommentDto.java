package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/22 09:48
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QmCommentDto {
    private String userName;
    private String comment;
    private Date createTime;
}
