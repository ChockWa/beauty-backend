package com.chockwa.beauty.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/5 13:56
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DownloadLog {

    private Long id;
    private String uid;
    private String sourceId;
    private Date createTime;

}
