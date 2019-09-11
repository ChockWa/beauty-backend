package com.chockwa.beauty.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_card")
public class Card {

    @TableId(type = IdType.INPUT)
    private String cardNo;
    private String uid;
    /**
     * 卡類型1-30元2-50元3-包月4-包季
     */
    private Integer type;
    private Integer status;
    private Date createTime;
    private Date useTime;

}
