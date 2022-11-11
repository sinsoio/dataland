package io.sinso.dataland.common.annotation;

import io.sinso.dataland.common.annotation.impl.UpperValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author alibeibei
 */
@Documented
@Constraint(
        validatedBy = {UpperValidatorImpl.class}
)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Upper {
    String message() default "Must be in uppercase";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
