package io.sinso.dataland.util;

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
