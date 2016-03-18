package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to signify a dependency on a named software system.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsesSoftwareSystems.class)
public @interface UsesSoftwareSystem {

    String name();
    String description();

}
