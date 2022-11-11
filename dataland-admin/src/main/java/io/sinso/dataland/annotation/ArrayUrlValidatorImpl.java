package io.sinso.dataland.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.sinso.dataland.enums.ResCodeEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.util.RegularUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * UpperValidatorImpl
 *
 * @author : alibeibei
 * @date : 2020/12/24 11:54
 */
public class ArrayUrlValidatorImpl implements ConstraintValidator<ArrayUrl, String> {
    @Override
    public void initialize(ArrayUrl arrayUrl) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        Annotation annotation = ((ConstraintValidatorContextImpl) context).getConstraintDescriptor().getAnnotation();
        ArrayUrl arrayUrl = (ArrayUrl) annotation;
        if (arrayUrl.max() < arrayUrl.min()) {
            throw new BusinessException(ResCodeEnum.PARAMETER_ERROR);
        }
        if (arrayUrl.max() == 0 || arrayUrl.min() == 0) {
            return true;
        }
        if (StringUtils.isEmpty(s)) {
            throw new BusinessException(ResCodeEnum.PARAMETER_ERROR);
        }
        try {
            JSONArray jsonArray = JSON.parseArray(s);
            if (jsonArray.size() == 0
                    || jsonArray.size() > arrayUrl.max()
                    || jsonArray.size() < arrayUrl.min()) {
                throw new BusinessException(ResCodeEnum.PARAMETER_ERROR);
            }
            for (Object o : jsonArray) {
                String url = (String) o;
                if (StringUtils.isNotBlank(url)) {
                    if (!RegularUtil.isUrl(url)) {
                        throw new BusinessException(ResCodeEnum.PARAMETER_ERROR);
                    }
                } else {
                    throw new BusinessException(ResCodeEnum.PARAMETER_ERROR);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(ResCodeEnum.PARAMETER_ERROR);
        }

        return true;
    }
}
