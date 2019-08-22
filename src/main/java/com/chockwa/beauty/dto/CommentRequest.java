package com.chockwa.beauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/22 15:28
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentRequest {
    private String qmId;
    private String comment;
}
