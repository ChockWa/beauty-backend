package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/29 09:51
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {

    private int pageIndex = 1;
    private int pageSize = 20;
}
