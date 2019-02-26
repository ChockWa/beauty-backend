package com.chockwa.beauty.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @auther: zhuohuahe
 * @date: 2019/2/26 17:31
 * @description:
 */
@Data
@TableName("sys_user")
public class User {

    @TableId(type = IdType.UUID)
    private String uid;

    private String userName;

    private String password;

    private String salt;

    private String email;

    private String mobile;

    private Integer isVip;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private String avator;

}
