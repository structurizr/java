package com.structurizr.component.filter;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IncludeFullyQualifiedNameRegexFilterTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullSuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new IncludeFullyQualifiedNameRegexFilter(null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptySuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new IncludeFullyQualifiedNameRegexFilter(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> new IncludeFullyQualifiedNameRegexFilter(" "));
    }


    @Test
    void filter_ReturnsFalse_WhenTheTypeDoesNotMatchRegex() {
        assertFalse(new IncludeFullyQualifiedNameRegexFilter(".*Component").accept(new Type("com.example.DateUtils")));
    }

    @Test
    void filter_ReturnsTrue_WhenTheTypeMatchesRegex() {
        assertTrue(new IncludeFullyQualifiedNameRegexFilter(".*Component").accept(new Type("com.example.CustomerComponent")));
    }

}