package com.structurizr.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AutomaticLayoutTests {

    @Test
    void setAutomaticLayout() {
        AutomaticLayout automaticLayout = new AutomaticLayout(AutomaticLayout.Implementation.Dagre, AutomaticLayout.RankDirection.LeftRight, 100, 200, 300, true);

        assertEquals(AutomaticLayout.RankDirection.LeftRight, automaticLayout.getRankDirection());
        assertEquals(100, automaticLayout.getRankSeparation());
        assertEquals(200, automaticLayout.getNodeSeparation());
        assertEquals(300, automaticLayout.getEdgeSeparation());
        assertTrue(automaticLayout.isVertices());
        assertFalse(automaticLayout.isApplied());
    }

    @Test
    void setRankDirection_ThrowsAnException_WhenNullIsSpecified() {
        try {
            AutomaticLayout automaticLayout = new AutomaticLayout();
            automaticLayout.setRankDirection(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A rank direction must be specified.", iae.getMessage());
        }
    }

    @Test
    void setRankSeparation_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            AutomaticLayout automaticLayout = new AutomaticLayout();
            automaticLayout.setRankSeparation(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The rank separation must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    void setNodeSeparation_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            AutomaticLayout automaticLayout = new AutomaticLayout();
            automaticLayout.setNodeSeparation(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The node separation must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    void setEdgeSeparation_ThrowsAnException_WhenANegativeIntegerIsSpecified() {
        try {
            AutomaticLayout automaticLayout = new AutomaticLayout();
            automaticLayout.setEdgeSeparation(-100);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The edge separation must be a positive integer.", iae.getMessage());
        }
    }

}