package com.chockwa.beauty.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@TableName("sys_coupon")
@Getter
@Setter
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;
}
