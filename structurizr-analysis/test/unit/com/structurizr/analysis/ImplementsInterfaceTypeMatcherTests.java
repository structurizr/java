package com.structurizr.analysis;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ImplementsInterfaceTypeMatcherTests {

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnExceptionWhenANonInterfaceTypeIsSupplied()
    {
        new ImplementsInterfaceTypeMatcher(MyRepositoryImpl.class, "", "");
    }

    @Test
    public void test_matches_ReturnsFalse_WhenTheGivenTypeDoesNotImplementTheInterface()
    {
        ImplementsInterfaceTypeMatcher matcher = new ImplementsInterfaceTypeMatcher(MyRepository.class, "", "");
        assertFalse(matcher.matches(MyController.class));
    }

    @Test
    public void test_matches_ReturnsTrue_WhenTheGivenTypeDoesImplementTheInterface()
    {
        ImplementsInterfaceTypeMatcher matcher = new ImplementsInterfaceTypeMatcher(MyRepository.class, "", "");
        assertTrue(matcher.matches(MyRepositoryImpl.class));
    }

    private class MyController {
    }

    private interface MyRepository {
    }

    private class MyRepositoryImpl implements MyRepository {
    }

}
