package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.*;

public class AutomaticLayoutTests {

    @Test
    public void test_setAutomaticLayout() {
        AutomaticLayout automaticLayout = new AutomaticLayout(AutomaticLayout.Implementation.Dagre, AutomaticLayout.RankDirection.LeftRight, 100, 200, 300, true);

        assertEquals(AutomaticLayout.RankDirection.LeftRight, automaticLayout.getRankDirection());
        assertEquals(100, automaticLayout.getRankSeparation());
        assertEquals(200, automaticLayout.getNodeSeparation());
        assertEquals(300, automaticLayout.getEdgeSeparation());
        assertTrue(automaticLayout.isVertices());
    }

    @Test
    public void test_setRankDirection_ThrowsAnException_WhenNullIsSpecified() {
        try {
            AutomaticLayout automaticLayout = new AutomaticLayout();
            automaticLayout.setRankDirection(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A rank direction must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_setRankSeparation_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            AutomaticLayout automaticLayout = new AutomaticLayout();
            automaticLayout.setRankSeparation(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The rank separation must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    public void test_setNodeSeparation_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            AutomaticLayout automaticLayout = new AutomaticLayout();
            automaticLayout.setNodeSeparation(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The node separation must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    public void test_setEdgeSeparation_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            AutomaticLayout automaticLayout = new AutomaticLayout();
            automaticLayout.setEdgeSeparation(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The edge separation must be a positive integer.", iae.getMessage());
        }
    }

}