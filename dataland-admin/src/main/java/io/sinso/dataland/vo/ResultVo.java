package io.sinso.dataland.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author alibeibei
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVo<T> implements Serializable {
    private static final long serialVersionUID = 8645597547986715292L;
    /**
     * Response time stamp
     */
    private Long ts;
    /**
     * Error code
     */
    private Integer errorCode;

    /**
     * errorMsg
     */
    private String errorMsg;
    /**
     * status
     */
    private String status;
    /**
     * data
     */
    private T data;

    private ResultVo() {
        setTs(System.currentTimeMillis());
        setStatus("ok");
    }

    private ResultVo(T object) {
        setTs(System.currentTimeMillis());
        setStatus("ok");
        setData(object);
    }

    private ResultVo(Integer errorCode) {
        setTs(System.currentTimeMillis());
        setErrorCode(errorCode);
        setStatus("fail");
    }

    private ResultVo(Integer errorCode, String errorMsg) {
        setTs(System.currentTimeMillis());
        setErrorCode(errorCode);
        setErrorMsg(errorMsg);
        setStatus("fail");
    }

    public static <T> ResultVo<T> success() {
        return new ResultVo<>();
    }

    public static <T> ResultVo<T> success(T data) {
        return new ResultVo<>(data);
    }

    public static <T> ResultVo<T> error(Integer errorCode) {
        return new ResultVo<>(errorCode);
    }

    public static <T> ResultVo<T> error(Integer errorCode, String errorMsg) {
        return new ResultVo<>(errorCode, errorMsg);
    }
}
