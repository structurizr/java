package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecisionTests extends AbstractWorkspaceTestBase {

    @Test
    void hasLinkTo() {
        Decision d1 = new Decision("1");
        Decision d2 = new Decision("2");
        Decision d3 = new Decision("3");

        d1.addLink(d2, "Type");

        assertTrue(d1.hasLinkTo(d2));
        assertFalse(d1.hasLinkTo(d3));
    }

}