package io.sinso.dataland.interceptor;

import io.sinso.dataland.enums.Headers;
import io.sinso.dataland.enums.ResCodeEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.model.Account;
import io.sinso.dataland.model.User;
import io.sinso.dataland.service.IAccessRecordService;
import io.sinso.dataland.service.IAccountService;
import io.sinso.dataland.service.IDauService;
import io.sinso.dataland.service.IUserService;
import io.sinso.dataland.util.UserLoginToken;
import io.sinso.dataland.util.UserUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Login verification interceptor
 *
 * @author alibeibei
 */
@Data
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IAccessRecordService accessRecordService;

    @Autowired
    private IDauService dauService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (checkIsNeedUserLoginToken(handlerMethod)) {
            if (checkIsNeedUserLoginToken(handlerMethod)) {
                String token = request.getHeader(Headers.TOKEN.getHeader());
                if (StringUtils.isEmpty(token)) {
                    throw new BusinessException(ResCodeEnum.REQUEST_TOKEN_ERROR);
                }
                //data land
                if (token.length() == 36) {
                    User user = userService.getToken(token);
                    if (user == null) {
                        throw new BusinessException(ResCodeEnum.REQUEST_TOKEN_ERROR);
                    }
                    if (StringUtils.isEmpty(user.getToken())) {
                        throw new BusinessException(ResCodeEnum.REQUEST_TOKEN_ERROR);
                    }
                    UserUtil.setUid(user.getId());
                    dauService.addDau(user.getId(), user.getAddress());
                }
                //Verify the key of the external api interface
                else if (token.length() == 12) {
                    Account account = accountService.getApiKey(token);
                    if (account == null) {
                        throw new BusinessException(ResCodeEnum.REQUEST_TOKEN_ERROR);
                    }
                    String servletPath = request.getServletPath();
                    accessRecordService.validationDayAccessNum(account.getId(), servletPath);
                    accessRecordService.validationSecondsAccessNum(account.getId(), servletPath);
                    UserUtil.setUid(account.getId());
                } else {
                    throw new BusinessException(ResCodeEnum.REQUEST_TOKEN_ERROR);
                }

            }
        }
        return true;
    }

    private boolean checkIsNeedUserLoginToken(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        Class<?> beanType = handlerMethod.getBeanType();
        UserLoginToken methodAnnotation = method.getAnnotation(UserLoginToken.class);
        UserLoginToken classAnnotation = beanType.getAnnotation(UserLoginToken.class);
        boolean verifyToken = false;
        boolean classToken = false;
        if (classAnnotation != null) {
            classToken = classAnnotation.required();
        }
        if (classToken) {
            if (methodAnnotation == null) {
                verifyToken = true;
            } else {
                verifyToken = methodAnnotation.required();
            }
        } else {
            if (methodAnnotation != null) {
                verifyToken = methodAnnotation.required();
            }
        }
        return verifyToken;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
