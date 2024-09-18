package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A type-level annotation that can be used to add a name-value property to the model element represented by the type.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Properties.class)
public @interface Property {

    String name();
    String value();

}