package com.structurizr.component.filter;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExcludeFullyQualifiedNameRegexFilterTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullSuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExcludeFullyQualifiedNameRegexFilter(null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptySuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExcludeFullyQualifiedNameRegexFilter(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExcludeFullyQualifiedNameRegexFilter(" "));
    }


    @Test
    void filter_ReturnsTrue_WhenTheTypeDoesNotMatchRegex() {
        assertTrue(new ExcludeFullyQualifiedNameRegexFilter(".*Utils").accept(new Type("com.example.CustomerComponent")));
    }

    @Test
    void filter_ReturnsFalse_WhenTheTypeMatchesRegex() {
        assertFalse(new ExcludeFullyQualifiedNameRegexFilter(".*Utils").accept(new Type("com.example.DateUtils")));
    }

}