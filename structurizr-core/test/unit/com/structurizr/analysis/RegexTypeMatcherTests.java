package com.structurizr.analysis;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegexTypeMatcherTests {

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnExceptionWhenANullRegexAsAStringIsSupplied()
    {
        new RegexTypeMatcher((String)null, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnExceptionWhenANullRegexAsAPatternIsSupplied()
    {
        new RegexTypeMatcher((Pattern)null, "", "");
    }

    @Test
    public void test_matches_ReturnsFalse_WhenTheNameOfTheGivenTypeDoesNotMatchTheRegex()
    {
        RegexTypeMatcher matcher = new RegexTypeMatcher(Pattern.compile("MyController"), "", "");
        assertFalse(matcher.matches(MyController.class));

        matcher = new RegexTypeMatcher("MyController", "", "");
        assertFalse(matcher.matches(MyController.class));
    }

    @Test
    public void test_matches_ReturnsTrue_WhenTheNameOfTheGivenTypeDoesMatchTheRegex()
    {
        String regex = ".*\\.analysis\\..*Controller";

        RegexTypeMatcher matcher = new RegexTypeMatcher(Pattern.compile(regex), "", "");
        assertTrue(matcher.matches(MyController.class));

        matcher = new RegexTypeMatcher(regex, "", "");
        assertTrue(matcher.matches(MyController.class));
    }

    @Test
    public void test_matches_ReturnsFalse_WhenPassedANullType() {
        String regex = ".*\\.analysis\\..*Controller";

        RegexTypeMatcher matcher = new RegexTypeMatcher(Pattern.compile(regex), "", "");
        assertFalse(matcher.matches(null));
    }

    private class MyController {
    }

}