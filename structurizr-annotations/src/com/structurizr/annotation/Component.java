package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A type-level annotation that can be used to signify that the annotated type
 * (an interface or class) can be considered to be a "component".
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    String description() default "";
    String technology() default "";

}
