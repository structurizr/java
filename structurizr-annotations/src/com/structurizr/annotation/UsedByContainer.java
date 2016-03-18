package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to signify a that a named container uses this component.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsedByContainers.class)
public @interface UsedByContainer {

    String name();
    String description();

}
