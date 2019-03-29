package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/29 09:49
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    private long total;

    private List<T> records;
}
