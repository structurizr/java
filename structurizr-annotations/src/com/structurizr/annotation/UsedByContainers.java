package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A wrapper for multiple @UsedByContainer annotations.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsedByContainers {

    UsedByContainer[] value();

}
