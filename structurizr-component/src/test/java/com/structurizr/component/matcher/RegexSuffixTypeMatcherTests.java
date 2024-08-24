package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegexSuffixTypeMatcherTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullRegex() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new RegexTypeMatcher(null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptyRegex() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new RegexTypeMatcher(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> new RegexTypeMatcher(" "));
    }

    @Test
    void matches_ThrowsAnException_WhenPassedNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new RegexTypeMatcher(".*Controller").matches(null));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoMatch() {
        assertFalse(new RegexTypeMatcher(".*Controller").matches(new Type("com.example.SomeClass")));
    }

    @Test
    void matches_ReturnsTrue_WhenThereIsAMatch() {
        assertTrue(new RegexTypeMatcher(".*Controller").matches(new Type("com.example.SomeController")));
    }

}