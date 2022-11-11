package io.sinso.dataland.common.annotation;

import java.lang.annotation.*;

/**
 * 需要渠道验证
 *
 * @author alibeibei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UserChannel {
    boolean required() default true;
}
