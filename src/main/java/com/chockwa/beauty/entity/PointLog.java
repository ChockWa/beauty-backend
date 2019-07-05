package com.chockwa.beauty.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/5 13:55
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointLog {

    private Long id;
    private String uid;
    private int point;
    private String description;
    private Date createTime;

}
