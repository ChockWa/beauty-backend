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
 * @date: 2019/9/17 09:24
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_qm_confirm")
public class QmConfirm {
    @TableId(type = IdType.UUID)
    private String id;
    private String uid;
    private Integer area;
    private String name;
    private String description;
    private String cover;
    private String image;
    private String contact;
    private Integer status;
    private Date createTime;
}
