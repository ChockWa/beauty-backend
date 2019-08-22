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
 * @date: 2019/8/22 08:45
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName("sys_qm_comment")
public class QmComment {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String uid;
    private String qmId;
    private String comment;
    private Date createTime;

}
