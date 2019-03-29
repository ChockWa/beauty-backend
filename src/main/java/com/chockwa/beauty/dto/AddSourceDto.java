package com.chockwa.beauty.dto;

import com.chockwa.beauty.entity.Source;
import com.chockwa.beauty.entity.SourceDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/29 10:16
 * @description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddSourceDto {

    private Source source;

    private List<SourceDetail> sourceDetailList;
}
