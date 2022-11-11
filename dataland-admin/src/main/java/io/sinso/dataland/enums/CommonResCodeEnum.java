package io.sinso.dataland.enums;

import lombok.Getter;

/**
 * @author alibeibei
 */

@Getter
public enum CommonResCodeEnum {
    //枚举10000
    SUCCESS(200, "Operation is successful"),
    FAIL(500, "The operation failure"),
    NOT_AUTH_OPERATE(555, "Have the right to operate"),
    ;
    private Integer errorCode;

    private String errorMsg;

    CommonResCodeEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
