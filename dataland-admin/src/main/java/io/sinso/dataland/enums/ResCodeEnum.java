package io.sinso.dataland.enums;

import lombok.Getter;

/**
 * 错误码枚举
 *
 * @author lee
 * @date 2019/11/1
 */
@Getter
public enum ResCodeEnum {
    SYSTEM_ERROR(40000, "system error"),
    REQUEST_TOKEN_ERROR(40001, "request token error"),
    PARAMETER_ERROR(40002, "parameter error"),
    VERIFY_CODE_ERROR(40003, "verify code error"),
    ACCOUNT_NOT_EXIST(40004, "account not exist"),
    NO_DATA(41000, "Data does not exist"),
    ALREADY_FAVORITED(41001, "Already favorited"),
    THIS_NAME_IS_ALREADY_USED(42002, "This name is already used"),
    THIS_FIELD_IS_REQUIRED(42003, "This field is required"),
    INCORRECT_URL(42004, "Incorrect url"),
    NFT_FAVORITED_UNSUCCESSFUL(42005, "NFT Favorited Unsuccessful"),
    UPLOAD_ERROR(42006, "upload error"),
    API_KEY_NUM_INSUFFICIENT(42007, "api key num insufficient"),
    ALREADY_APPLY(42008, "Already apply"),
    DAY_ACCESS_LIMIT(42009, "The maximum number of accesses per day was exceeded"),
    SECONDS_ACCESS_LIMIT(42010, "Exceeding the number of accesses per second"),
    WALLET_ADDRESS_ERROR(42011, "walletAddress error"),

    ;
    private Integer errorCode;

    private String errorMsg;

    ResCodeEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
