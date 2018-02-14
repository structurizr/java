package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * A field-level annotation that can be used to supplement the existing relationship
 * (i.e. add a description and/or technology) between two components.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsesComponent {

    String description();
    String technology() default "";

}
