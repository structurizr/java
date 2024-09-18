package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A type-level annotation that can be used to indicate the type represents a component.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}