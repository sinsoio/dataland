package io.sinso.dataland.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lee
 * @version V4.0
 * @title RequestLog
 * @desc log
 * @date : 19-1-10 下午3:56
 */
@Aspect
@Component
@Slf4j
public class RequestLog {

    private ThreadLocal<Long> startTime = new ThreadLocal<>();
    private Map<String, Object> requestMap = new HashMap<>();

    @Autowired
    private DynamicConfig dynamicConfig;

    /**
     * Defining a pointcut
     */
    @Pointcut("execution(public * io.sinso.dataland.controller.*.*(..))")
    public void requestLog() {

    }

    /**
     * Logging before arriving at the method
     */
    @Before(value = "requestLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        requestMap.put("request-time", LocalDateTime.now().toString());
        requestMap.put("url", request.getRequestURL().toString());
        requestMap.put("class-method", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        requestMap.put("http-method", request.getMethod());
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (Object objects : args) {
            params.append(objects).append(",");
        }
        if (!"".equals(params.toString())) {
            params = new StringBuilder(params.substring(0, params.length() - 1));
            params = new StringBuilder("(" + params + ")");
        }
        requestMap.put("params", params.toString());
    }

    /**
     * After the method executes
     */
    @After(value = "requestLog()")
    public void doAfter() {
        requestMap.put("endTime", System.currentTimeMillis() - startTime.get());
        if (dynamicConfig.getLogSwitch()) {
            log.info(JSON.toJSONString(requestMap));
        }
        startTime.remove();
    }
}
