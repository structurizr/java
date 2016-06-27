package com.structurizr.componentfinder;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InterfaceImplementationTypeMatcherTests {

    @Test
    public void test_construction_DoesNotThrowAnExceptionWhenAnInterfaceTypeIsSupplied()
    {
        InterfaceImplementationTypeMatcher matcher = new InterfaceImplementationTypeMatcher(MyRepository.class, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Test_construction_ThrowsAnExceptionWhenANonInterfaceTypeIsSupplied()
    {
        InterfaceImplementationTypeMatcher matcher = new InterfaceImplementationTypeMatcher(MyRepositoryImpl.class, "", "");
    }

    @Test
    public void Test_matches_ReturnsFalse_WhenTheGivenTypeDoesNotImplementTheInterface()
    {
        InterfaceImplementationTypeMatcher matcher = new InterfaceImplementationTypeMatcher(MyRepository.class, "", "");
        assertFalse(matcher.matches(MyController.class));
    }

    @Test
    public void Test_matches_ReturnsTrue_WhenTheGivenTypeDoesImplementTheInterface()
    {
        InterfaceImplementationTypeMatcher matcher = new InterfaceImplementationTypeMatcher(MyRepository.class, "", "");
        assertTrue(matcher.matches(MyRepositoryImpl.class));
    }

    private class MyController {
    }

    private interface MyRepository {
    }

    private class MyRepositoryImpl implements MyRepository {
    }

}
