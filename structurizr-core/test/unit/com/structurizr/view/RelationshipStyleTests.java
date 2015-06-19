package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RelationshipStyleTests {

    private RelationshipStyle relationshipStyle = new RelationshipStyle("tag");

    @Test
    public void test_setPosition_SetsPositionToNull_WhenNullIsSpecified() {
        relationshipStyle.setPosition(null);
        assertNull(relationshipStyle.getPosition());
    }

    @Test
    public void test_setPosition_SetsPositionToZero_WhenANegativeNumberIsSpecified() {
        relationshipStyle.setPosition(-1);
        assertEquals(new Integer(0), relationshipStyle.getPosition());
    }

    @Test
    public void test_setPosition_SetsPositionToOneHundred_WhenANumberGreaterThanOneHundredIsSpecified() {
        relationshipStyle.setPosition(101);
        assertEquals(new Integer(100), relationshipStyle.getPosition());
    }

    @Test
    public void test_setPosition_SetsPosition_WhenANumberBetweenZeroAndOneHundredIsSpecified() {
        relationshipStyle.setPosition(0);
        assertEquals(new Integer(0), relationshipStyle.getPosition());

        relationshipStyle.setPosition(1);
        assertEquals(new Integer(1), relationshipStyle.getPosition());

        relationshipStyle.setPosition(50);
        assertEquals(new Integer(50), relationshipStyle.getPosition());


        relationshipStyle.setPosition(99);
        assertEquals(new Integer(99), relationshipStyle.getPosition());

        relationshipStyle.setPosition(100);
        assertEquals(new Integer(100), relationshipStyle.getPosition());
    }

}
