package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A wrapper for @Tag annotations.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tags {

    Tag[] value();

}