package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A wrapper for @Property annotations.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Properties {

    Property[] value();

}