package io.sinso.dataland.common.annotation.impl;

import io.sinso.dataland.common.annotation.Upper;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * UpperValidatorImpl
 *
 * @author : alibeibei
 * @date : 2020/12/24 11:54
 */
public class UpperValidatorImpl implements ConstraintValidator<Upper, String> {
    @Override
    public void initialize(Upper constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equals(s.toUpperCase());
    }
}
