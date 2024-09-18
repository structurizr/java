package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A type-level annotation that can be used to add a tag to the model element represented by the type.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Tags.class)
public @interface Tag {

    String name();

}