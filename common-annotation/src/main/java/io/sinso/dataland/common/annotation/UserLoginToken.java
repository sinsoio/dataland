package io.sinso.dataland.common.annotation;

import java.lang.annotation.*;

/**
 * @author alibeibei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UserLoginToken {
    boolean required() default true;
}
