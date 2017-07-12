package com.structurizr.analysis;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NameSuffixTypeMatcherTests {

    @Test
    public void test_construction_DoesNotThrowAnExceptionWhenASuffixIsSupplied()
    {
        NameSuffixTypeMatcher matcher = new NameSuffixTypeMatcher("Component", "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Test_construction_ThrowsAnExceptionWhenANullSuffixIsSupplied()
    {
        NameSuffixTypeMatcher matcher = new NameSuffixTypeMatcher(null, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Test_construction_ThrowsAnExceptionWhenAnEmptyStringSuffixIsSupplied()
    {
        NameSuffixTypeMatcher matcher = new NameSuffixTypeMatcher(" ", "", "");
    }

    @Test
    public void Test_matches_ReturnsFalse_WhenTheNameOfTheGivenTypeDoesNotHaveTheSuffix()
    {
        NameSuffixTypeMatcher matcher = new NameSuffixTypeMatcher("Component", "", "");
        assertFalse(matcher.matches(MyController.class));
    }

    @Test
    public void Test_Matches_ReturnsTrue_WhenTheNameOfTheGivenTypeDoesHaveTheSuffix()
    {
        NameSuffixTypeMatcher matcher = new NameSuffixTypeMatcher("Controller", "", "");
        assertTrue(matcher.matches(MyController.class));
    }

    private class MyController {
    }

}
