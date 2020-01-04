package com.chockwa.beauty.constant;

import lombok.Getter;

@Getter
public enum  QmType {
    QM(1),
    SN(2),
    VIDEO(3);

    private int code;

    QmType(int code) {
        this.code = code;
    }
}
