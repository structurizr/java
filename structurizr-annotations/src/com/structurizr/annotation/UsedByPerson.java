package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A type-level annotation that can be used to signify that the named
 * person uses the component on which this annotation is placed,
 * creating a relationship from the person to the component.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsedByPeople.class)
public @interface UsedByPerson {

    String name();
    String description() default "";
    String technology() default "";

}
