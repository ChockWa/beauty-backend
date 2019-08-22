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
 * @date: 2019/8/22 09:15
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName("sys_qm_buy_log")
public class QmBuyLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String uid;
    private String qmId;
    private Date createTime;

    public QmBuyLog(String uid, String qmId, Date createTime){
        this.uid = uid;
        this.qmId = qmId;
        this.createTime = createTime;
    }
}
