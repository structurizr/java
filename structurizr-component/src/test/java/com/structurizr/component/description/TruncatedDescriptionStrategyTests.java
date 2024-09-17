package com.structurizr.component.description;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TruncatedDescriptionStrategyTests {

    @Test
    public void test_construction_ThrowsAnIllegalArgumentException_WhenZeroIsSpecified() {
        try {
            new TruncatedDescriptionStrategy(0);
        } catch (Exception e) {
            assertEquals("Max length must be greater than 0", e.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnIllegalArgumentException_WhenANegativeNumberIsSpecified() {
        try {
            new TruncatedDescriptionStrategy(-1);
        } catch (Exception e) {
            assertEquals("Max length must be greater than 0", e.getMessage());
        }
    }

    @Test
    public void test_descriptionOf_TruncatesTheDescription()
    {
        Type type = new Type("Name");
        type.setDescription("Here is some text.");
        assertEquals("Here...", new TruncatedDescriptionStrategy(4).descriptionOf(type));
    }

}