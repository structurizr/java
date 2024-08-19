package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;
import org.apache.bcel.classfile.AnnotationEntry;

import java.lang.annotation.Annotation;

/**
 * Matches types based upon the presence of a type-level annotation.
 */
public class AnnotationTypeMatcher extends AbstractTypeMatcher {

    private final String annotationType;

    public AnnotationTypeMatcher(String annotationType, String technology) {
        super(technology);

        if (StringUtils.isNullOrEmpty(annotationType)) {
            throw new IllegalArgumentException("An annotation type must be supplied");
        }

        this.annotationType = "L" + annotationType.replace(".", "/") + ";";
    }

    public AnnotationTypeMatcher(Class<? extends Annotation> annotation, String technology) {
        super(technology);

        if (annotation == null) {
            throw new IllegalArgumentException("An annotation must be supplied");
        }

        this.annotationType = "L" + annotation.getCanonicalName().replace(".", "/") + ";";
    }

    @Override
    public boolean matches(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("A type must be specified");
        }

        if (type.getJavaClass() == null) {
            return false;
        }

        AnnotationEntry[] annotationEntries = type.getJavaClass().getAnnotationEntries();
        for (AnnotationEntry annotationEntry : annotationEntries) {
            if (annotationType.equals(annotationEntry.getAnnotationType())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "AnnotationTypeMatcher{" +
                "annotationType='" + annotationType + '\'' +
                '}';
    }

}