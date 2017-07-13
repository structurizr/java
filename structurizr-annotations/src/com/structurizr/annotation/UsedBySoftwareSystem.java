package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A type-level annotation that can be used to signify that the named
 * software system uses the component on which this annotation is placed,
 * creating a relationship from the software system to the component.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsedBySoftwareSystems.class)
public @interface UsedBySoftwareSystem {

    String name();
    String description() default "";
    String technology() default "";

}
