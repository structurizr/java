package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NameSuffixTypeMatcherTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullSuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new NameSuffixTypeMatcher(null, "Technology"));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptySuffix() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new NameSuffixTypeMatcher("", "Technology"));
        assertThrowsExactly(IllegalArgumentException.class, () -> new NameSuffixTypeMatcher(" ", "Technology"));
    }

    @Test
    void matches_ThrowsAnException_WhenPassedNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new NameSuffixTypeMatcher("Suffix", "Technology").matches(null));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoMatch() {
        assertFalse(new NameSuffixTypeMatcher("Component", "Technology").matches(new Type("com.example.SomeClass")));
    }

    @Test
    void matches_ReturnsTrue_WhenThereIsAMatch() {
        assertTrue(new NameSuffixTypeMatcher("Component", "Technology").matches(new Type("com.example.SomeComponent")));
    }

}