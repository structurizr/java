package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A wrapper for multiple @UsesContainer annotations.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsesContainers {

    UsesContainer[] value();

}
