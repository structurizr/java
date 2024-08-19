package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegexSuffixTypeMatcherTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullRegex() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new RegexTypeMatcher(null, "Technology"));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptyRegex() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new RegexTypeMatcher("", "Technology"));
        assertThrowsExactly(IllegalArgumentException.class, () -> new RegexTypeMatcher(" ", "Technology"));
    }

    @Test
    void matches_ThrowsAnException_WhenPassedNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new RegexTypeMatcher(".*Controller", "Technology").matches(null));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoMatch() {
        assertFalse(new RegexTypeMatcher(".*Controller", "Technology").matches(new Type("com.example.SomeClass")));
    }

    @Test
    void matches_ReturnsTrue_WhenThereIsAMatch() {
        assertTrue(new RegexTypeMatcher(".*Controller", "Technology").matches(new Type("com.example.SomeController")));
    }

}