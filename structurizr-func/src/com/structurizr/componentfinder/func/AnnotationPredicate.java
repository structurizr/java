package com.structurizr.componentfinder.func;

import com.google.common.base.Preconditions;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

class AnnotationPredicate implements Predicate<Class<?>> {

    private final Class<? extends Annotation> annotation;

    private AnnotationPredicate(Class<? extends Annotation> annotation) {
        Preconditions.checkNotNull(annotation, "An annotation must be supplied");
        this.annotation = annotation;
    }

    public static Predicate<Class<?>> create(Class<? extends Annotation> annotation) {
        return new AnnotationPredicate(annotation);
    }

    @Override
    public boolean test(Class<?> type) {
        return type.getAnnotation(annotation) != null;
    }

}
