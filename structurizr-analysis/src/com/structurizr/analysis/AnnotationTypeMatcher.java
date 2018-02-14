package com.structurizr.analysis;

import java.lang.annotation.Annotation;

/**
 * Matches types based upon the presence of a type-level annotation.
 */
public class AnnotationTypeMatcher extends AbstractTypeMatcher {

    private Class<? extends Annotation> annotation;

    public AnnotationTypeMatcher(Class<? extends Annotation> annotation, String description, String technology) {
        super(description, technology);

        if (annotation == null) {
            throw new IllegalArgumentException("An annotation must be supplied");
        }

        this.annotation = annotation;
    }

    @Override
    public boolean matches(Class type) {
        return type.getAnnotation(annotation) != null;
    }

}