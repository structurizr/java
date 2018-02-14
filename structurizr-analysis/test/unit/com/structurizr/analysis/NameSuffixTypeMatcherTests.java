package com.structurizr.analysis;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NameSuffixTypeMatcherTests {

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnExceptionWhenANullSuffixIsSupplied()
    {
        new NameSuffixTypeMatcher(null, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnExceptionWhenAnEmptyStringSuffixIsSupplied()
    {
        new NameSuffixTypeMatcher(" ", "", "");
    }

    @Test
    public void test_matches_ReturnsFalse_WhenTheNameOfTheGivenTypeDoesNotHaveTheSuffix()
    {
        NameSuffixTypeMatcher matcher = new NameSuffixTypeMatcher("Component", "", "");
        assertFalse(matcher.matches(MyController.class));
    }

    @Test
    public void Ttest_matches_ReturnsTrue_WhenTheNameOfTheGivenTypeDoesHaveTheSuffix()
    {
        NameSuffixTypeMatcher matcher = new NameSuffixTypeMatcher("Controller", "", "");
        assertTrue(matcher.matches(MyController.class));
    }

    private class MyController {
    }

}