package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A wrapper for multiple @UsedBySoftwareSystem annotations.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsedBySoftwareSystems {

    UsedBySoftwareSystem[] value();

}
