package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A type-level annotation that can be used to signify that the component
 * on which this annotation is placed has a relationship to the named software system,
 * creating a relationship from the component to the software system.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsesSoftwareSystems.class)
public @interface UsesSoftwareSystem {

    String name();
    String description() default "";
    String technology() default "";

}
