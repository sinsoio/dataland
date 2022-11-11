package io.sinso.dataland.exception;

import io.sinso.dataland.enums.CommonResCodeEnum;
import io.sinso.dataland.enums.ResCodeEnum;
import lombok.Getter;

import java.io.Serializable;

/**
 * Business exceptions
 *
 * @author lee
 */
@Getter
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 5339962879722142051L;

    /**
     * Abnormal coding
     */
    private Integer errCode;

    /**
     * 异常
     */
    private String errMsg;


    public BusinessException(Integer errCode, String errMsg) {
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BusinessException(ResCodeEnum resCodeEnum) {
        super();
        this.errCode = resCodeEnum.getErrorCode();
        this.errMsg = resCodeEnum.getErrorMsg();
    }

    public BusinessException(CommonResCodeEnum commonResCodeEnum) {
        super();
        this.errCode = commonResCodeEnum.getErrorCode();
        this.errMsg = commonResCodeEnum.getErrorMsg();
    }
}
