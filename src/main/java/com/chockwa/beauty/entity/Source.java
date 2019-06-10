package com.chockwa.beauty.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_source
 * @author 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_source")
public class Source implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 分类
     */
    private Integer category;

    /**
     * 所屬機構
     */
    private Integer org;

    /**
     * 人物
     */
    private String model;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面
     */
    private String cover;

    /**
     * 预览图片
     */
    private String pics;

    /**
     * 时长单位:分钟
     */
    private Integer time;

    /**
     * 下载码
     */
    private String downloadCode;

    /**
     * 下载地址
     */
    private String downloadLink;

    /**
     * 下载地址
     */
    private String zipDownloadLink;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}