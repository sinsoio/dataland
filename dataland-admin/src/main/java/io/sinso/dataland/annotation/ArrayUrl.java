package io.sinso.dataland.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author alibeibei
 */
@Documented
@Constraint(
        validatedBy = {ArrayUrlValidatorImpl.class}
)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayUrl {
    String message() default "Must be in lower case";

    int min() default 0;

    int max() default 0;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
