package com.structurizr.componentfinder.func;

import com.structurizr.componentfinder.TestConstants;
import com.structurizr.testapp.myapp.MyController;
import com.structurizr.testapp.myapp.MyRepository;
import org.junit.Test;

import java.util.function.Predicate;

import static com.structurizr.componentfinder.TestConstants.MATCH_ALL_UNDER_MYAPP_PACKAGE_REGEX;
import static com.structurizr.componentfinder.TestConstants.MATCH_CONTROLLER_SUFFIX_IN_MY_APP_REGEX;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegexClassNamePredicateTest {
    @Test
    public void matches() {
        final Predicate<Class<?>> typeMatcher = RegexClassNamePredicate.create(TestConstants.MATCH_ALL_UNDER_MYAPP_PACKAGE_REGEX);
        assertFalse(typeMatcher.test(this.getClass()));
        assertTrue(typeMatcher.test(MyController.class));
        assertTrue(typeMatcher.test(MyRepository.class));
    }

    @Test
    public void matchRegex() {
        final Predicate<Class<?>> typeMatcher = RegexClassNamePredicate.create(MATCH_ALL_UNDER_MYAPP_PACKAGE_REGEX);
        assertFalse(typeMatcher.test(this.getClass()));
        assertTrue(typeMatcher.test(MyController.class));
        assertTrue(typeMatcher.test(MyRepository.class));
    }

    @Test
    public void matchSingleClass() {
        final Predicate<Class<?>> typeMatcher = RegexClassNamePredicate.create(MATCH_CONTROLLER_SUFFIX_IN_MY_APP_REGEX);
        assertFalse(typeMatcher.test(this.getClass()));
        assertTrue(typeMatcher.test(MyController.class));
        assertFalse(typeMatcher.test(MyRepository.class));
    }

}