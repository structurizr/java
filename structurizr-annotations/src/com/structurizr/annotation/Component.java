package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * This annotation can be used to signify that the type can be considered to be a "component".
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    String description() default "";
    String technology() default "";

}
