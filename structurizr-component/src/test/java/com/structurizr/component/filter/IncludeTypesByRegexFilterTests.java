package com.structurizr.component.filter;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IncludeTypesByRegexFilterTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullSuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new IncludeTypesByRegexFilter(null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptySuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new IncludeTypesByRegexFilter(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> new IncludeTypesByRegexFilter(" "));
    }


    @Test
    void filter_ReturnsFalse_WhenTheTypeDoesNotMatchRegex() {
        assertFalse(new IncludeTypesByRegexFilter(".*Component").accept(new Type("com.example.DateUtils")));
    }

    @Test
    void filter_ReturnsTrue_WhenTheTypeMatchesRegex() {
        assertTrue(new IncludeTypesByRegexFilter(".*Component").accept(new Type("com.example.CustomerComponent")));
    }

}