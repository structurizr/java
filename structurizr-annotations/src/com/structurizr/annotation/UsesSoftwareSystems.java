package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A wrapper for multiple @UsesSoftwareSystem annotations.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsesSoftwareSystems {

    UsesSoftwareSystem[] value();

}
