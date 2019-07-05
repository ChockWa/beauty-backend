package com.chockwa.beauty.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_point_log")
public class PointLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String uid;
    private int point;
    private String description;
    private Date createTime;

}
