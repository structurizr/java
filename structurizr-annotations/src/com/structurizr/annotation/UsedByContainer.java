package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A type-level annotation that can be used to signify that the named
 * container uses the component on which this annotation is placed,
 * creating a relationship from the container to the component.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsedByContainers.class)
public @interface UsedByContainer {

    String name();
    String description() default "";
    String technology() default "";

}
