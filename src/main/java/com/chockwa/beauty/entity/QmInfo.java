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
 * @date: 2019/8/22 08:43
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName("sys_qm_info")
public class QmInfo {

    @TableId(type = IdType.UUID)
    private String id;
    private int area;
    private String name;
    private String cover;
    private String description;
    private String image;
    private String contact;
    private String score;
    private int price;
    private Date createTime;
    private Integer status;
    private Integer type;
    private String contactCode;
}
