package io.sinso.dataland.common.annotation;

import java.lang.annotation.*;

/**
 *
 * @author : alibeibei
 * @date : 2020/07/17 15:33
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuthorize {
    
    String[] value();
}
