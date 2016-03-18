package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to signify a that a named type of person uses this component.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsedByPeople.class)
public @interface UsedByPerson {

    String name();
    String description();

}
