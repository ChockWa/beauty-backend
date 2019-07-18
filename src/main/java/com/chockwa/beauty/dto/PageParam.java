package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/29 09:51
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageParam implements Serializable {
    private static final long serialVersionUID = -5184766291264306455L;
    private int pageIndex = 1;
    private int pageSize = 20;
}
