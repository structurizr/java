package com.structurizr.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to signify a dependency on the component, which is
 * based upon the type of the field this annotation is present on.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsesComponent {

    String description();

}
