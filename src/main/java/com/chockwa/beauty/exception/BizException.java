package com.chockwa.beauty.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 18:20
 * @description:
 */
@Getter
@Setter
public class BizException extends RuntimeException {

    private int code;

    private String message;

    public static BizException TOKEN_EXPIRE = new BizException(1000, "Token expired, please login again");

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(String message) {
        super(message);
        this.message = message;
    }
}
