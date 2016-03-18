package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to signify a dependency on a named container.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsesContainers.class)
public @interface UsesContainer {

    String name();
    String description();

}
