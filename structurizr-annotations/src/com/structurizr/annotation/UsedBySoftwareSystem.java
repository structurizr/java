package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to signify a that a named software system uses this component.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsedBySoftwareSystems.class)
public @interface UsedBySoftwareSystem {

    String name();
    String description();

}
