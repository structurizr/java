package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A wrapper for multiple @UsedByPerson annotations.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsedByPeople {

    UsedByPerson[] value();

}
