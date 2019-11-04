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

    public static BizException TOKEN_EXPIRE = new BizException(1000, "會話已過期，請重新登陸");

    public static BizException COIN_NOT_ENOUGH = new BizException(1001, "今天已领取过，需要金幣购买，金币不足請先充值");

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
