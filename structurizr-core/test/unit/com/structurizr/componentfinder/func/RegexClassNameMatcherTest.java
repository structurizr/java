package com.structurizr.componentfinder.func;

import com.structurizr.componentfinder.myapp.MyController;
import com.structurizr.componentfinder.myapp.MyRepository;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegexClassNameMatcherTest {
    @Test
    public void matches() {
        final Predicate<Class<?>> typeMatcher = RegexClassNameMatcher.create("com.structurizr.componentfinder.myapp..*");
        assertFalse(typeMatcher.test(this.getClass()));
        assertTrue(typeMatcher.test(MyController.class));
        assertTrue(typeMatcher.test(MyRepository.class));
    }

    @Test
    public void matchRegex() {
        final Predicate<Class<?>> typeMatcher = RegexClassNameMatcher.create("com\\..*\\.myapp\\..*");
        assertFalse(typeMatcher.test(this.getClass()));
        assertTrue(typeMatcher.test(MyController.class));
        assertTrue(typeMatcher.test(MyRepository.class));
    }

}