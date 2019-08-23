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
 * @date: 2019/8/23 09:16
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName("sys_sign_log")
public class SignLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String uid;
    private Date createTime;
}
