package io.sinso.dataland.common.annotation;

import io.sinso.dataland.common.annotation.impl.LowerValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author alibeibei
 */
@Documented
@Constraint(
        validatedBy = {LowerValidatorImpl.class}
)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lower {
    String message() default "Must be in lower case";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
