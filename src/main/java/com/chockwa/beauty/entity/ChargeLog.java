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
 * @date: 2019/8/22 08:48
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName("sys_charge_log")
public class ChargeLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String uid;
    private String chargeCode;
    private int chargeCoin;
    private int chargePrice;
    private Date createTime;
}
