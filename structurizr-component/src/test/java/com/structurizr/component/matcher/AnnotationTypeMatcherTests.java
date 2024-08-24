package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import org.apache.bcel.classfile.ClassParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotationTypeMatcherTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullName() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new AnnotationTypeMatcher((String)null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptyName() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new AnnotationTypeMatcher(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> new AnnotationTypeMatcher(" "));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedANullClass() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new AnnotationTypeMatcher((Class<? extends Annotation>) null));
    }

    @Test
    void matches_ThrowsAnException_WhenPassedNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new AnnotationTypeMatcher("com.example.AnnotationName").matches(null));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoUnderlyingJavaClass() {
        Type type = new Type("com.structurizr.component.matcher.annotationTypeMatcher.CustomerController");

        assertFalse(new AnnotationTypeMatcher("com.structurizr.component.matcher.annotationTypeMatcher.Controller").matches(type));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoMatch() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/matcher/annotationTypeMatcher/CustomerController.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        assertFalse(new AnnotationTypeMatcher("com.structurizr.component.matcher.annotationTypeMatcher.Repository").matches(type));
    }

    @Test
    void matches_ReturnsTrue_WhenThereIsAMatch() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/matcher/annotationTypeMatcher/CustomerController.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        assertTrue(new AnnotationTypeMatcher("com.structurizr.component.matcher.annotationTypeMatcher.Controller").matches(type));
    }

}