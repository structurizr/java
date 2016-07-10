package com.structurizr.componentfinder;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegexTypeMatcherTests {

    @Test
    public void test_construction_DoesNotThrowAnExceptionWhenARegexIsSupplied()
    {
        RegexTypeMatcher matcher = new RegexTypeMatcher(Pattern.compile("abc"), "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Test_construction_ThrowsAnExceptionWhenANullRegexIsSupplied()
    {
        RegexTypeMatcher matcher = new RegexTypeMatcher(null, "", "");
    }

    @Test
    public void Test_matches_ReturnsFalse_WhenTheNameOfTheGivenTypeDoesNotMatchTheRegex()
    {
        RegexTypeMatcher matcher = new RegexTypeMatcher(Pattern.compile("MyController"), "", "");
        assertFalse(matcher.matches(MyController.class));
    }

    @Test
    public void Test_Matches_ReturnsTrue_WhenTheNameOfTheGivenTypeDoesMatchTheRegex()
    {
        RegexTypeMatcher matcher = new RegexTypeMatcher(Pattern.compile(".*\\.MyController"), "", "");
        assertTrue(matcher.matches(MyController.class));
    }

    private class MyController {
    }

}
