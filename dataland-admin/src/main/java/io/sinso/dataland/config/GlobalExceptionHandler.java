package io.sinso.dataland.config;

import io.sinso.dataland.enums.CommonResCodeEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * Global exception handler
 *
 * @author lee
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private DynamicConfig dynamicConfig;

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultVo errorHandler(Exception ex) {
        if (dynamicConfig.getLogSwitch()) {
            ex.printStackTrace();
            log.error(String.format("ex=%s", ex.getMessage()), ex);
        }
        return ResultVo.error(CommonResCodeEnum.FAIL.getErrorCode(), "System is busy");
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ResultVo errorHandler(BusinessException ex) {
        if (dynamicConfig.getLogSwitch()) {
            ex.printStackTrace();
            log.error(String.format("ex=%s", ex.getMessage()), ex);
        }
        return ResultVo.error(ex.getErrCode(), ex.getErrMsg());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultVo errorHandler(MethodArgumentNotValidException ex) {
        if (dynamicConfig.getLogSwitch()) {
            ex.printStackTrace();
            log.error(String.format("ex=%s", ex.getMessage()), ex);
        }
        return ResultVo.error(500, "Parameters of the abnormal");
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public void handle404(NoHandlerFoundException ex) {
        if (dynamicConfig.getLogSwitch()) {
            ex.printStackTrace();
            log.error(String.format("ex=%s", ex.getMessage()), ex);
        }
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultVo errorHandler(MissingServletRequestParameterException e) {
        if (dynamicConfig.getLogSwitch()) {
            e.printStackTrace();
            log.error(String.format("ex=%s", e.getMessage()), e);
        }
        return ResultVo.error(500, "Parameters of the abnormal");
    }
}
