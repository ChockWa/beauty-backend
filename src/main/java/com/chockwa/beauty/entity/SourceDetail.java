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
 * sys_source_detail
 * @author 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_source_detail")
public class SourceDetail implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源id
     */
    private Long sourceId;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 带域名图片url
     */
    private String picUrl;

    /**
     * 不带域名地址
     */
    private String originUrl;

    /**
     * 删除地址
     */
    private String deleteUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

}