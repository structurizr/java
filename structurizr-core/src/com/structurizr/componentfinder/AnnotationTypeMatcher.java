package com.structurizr.componentfinder;

import java.lang.annotation.Annotation;

/**
 * Matches types based upon the presence of a type-level annotation.
 */
public class AnnotationTypeMatcher implements TypeMatcher {

    private Class<? extends Annotation> annotation;
    private String description;
    private String technology;

    public AnnotationTypeMatcher(Class<? extends Annotation> annotation, String description, String technology) {
        this.annotation = annotation;
        this.description = description;
        this.technology = technology;

        if (annotation == null) {
            throw new IllegalArgumentException("An annotation must be supplied");
        }
    }

    @Override
    public boolean matches(Class type) {
        return type.getAnnotation(annotation) != null;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTechnology() {
        return technology;
    }

}
