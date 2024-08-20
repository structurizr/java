package com.structurizr.component.filter;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExcludeTypesByRegexFilterTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullSuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExcludeTypesByRegexFilter(null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptySuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExcludeTypesByRegexFilter(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExcludeTypesByRegexFilter(" "));
    }


    @Test
    void filter_ReturnsTrue_WhenTheTypeDoesNotMatchRegex() {
        assertTrue(new ExcludeTypesByRegexFilter(".*Utils").accept(new Type("com.example.CustomerComponent")));
    }

    @Test
    void filter_ReturnsFalse_WhenTheTypeMatchesRegex() {
        assertFalse(new ExcludeTypesByRegexFilter(".*Utils").accept(new Type("com.example.DateUtils")));
    }

}