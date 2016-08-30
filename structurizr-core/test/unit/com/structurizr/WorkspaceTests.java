package com.structurizr;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WorkspaceTests {

    private Workspace workspace = new Workspace("Name", "Description");

    @Test
    public void test_setSource_DoeNotThrowAnException_WhenANullUrlIsSpecified() {
        workspace.setSource(null);
    }

    @Test
    public void test_setSource_DoeNotThrowAnException_WhenAnEmptyUrlIsSpecified() {
        workspace.setSource("");
    }

    @Test
    public void test_setSource_ThrowsAnException_WhenAnInvalidUrlIsSpecified() {
        try {
            workspace.setSource("www.somedomain.com");
            fail();
        } catch (Exception e) {
            assertEquals("www.somedomain.com is not a valid URL.", e.getMessage());
        }
    }

    @Test
    public void test_setSource_DoesNotThrowAnException_WhenAnValidUrlIsSpecified() {
        workspace.setSource("https://www.somedomain.com");
        assertEquals("https://www.somedomain.com", workspace.getSource());
    }

}
