package com.structurizr;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class WorkspaceTests {

    private Workspace workspace = new Workspace("Name", "Description");

    @Test
    public void test_setSource_DoesNotThrowAnException_WhenANullUrlIsSpecified() {
        workspace.setSource(null);
    }

    @Test
    public void test_setSource_DoesNotThrowAnException_WhenAnEmptyUrlIsSpecified() {
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

    @Test
    public void test_hasSource_ReturnsFalse_WhenANullSourceIsSpecified() {
        workspace.setSource(null);
        assertFalse(workspace.hasSource());
    }

    @Test
    public void test_hasSource_ReturnsFalse_WhenAnEmptySourceIsSpecified() {
        workspace.setSource(" ");
        assertFalse(workspace.hasSource());
    }

    @Test
    public void test_hasSource_ReturnsTrue_WhenAUrlIsSpecified() {
        workspace.setSource("https://www.somedomain.com");
        assertTrue(workspace.hasSource());
    }

    @Test
    public void test_setApi_DoesNotThrowAnException_WhenANullUrlIsSpecified() {
        workspace.setApi(null);
    }

    @Test
    public void test_setApi_DoesNotThrowAnException_WhenAnEmptyUrlIsSpecified() {
        workspace.setApi("");
    }

    @Test
    public void test_setApi_ThrowsAnException_WhenAnInvalidUrlIsSpecified() {
        try {
            workspace.setApi("www.somedomain.com");
            fail();
        } catch (Exception e) {
            assertEquals("www.somedomain.com is not a valid URL.", e.getMessage());
        }
    }

    @Test
    public void test_setApi_DoesNotThrowAnException_WhenAnValidUrlIsSpecified() {
        workspace.setApi("https://www.somedomain.com");
        assertEquals("https://www.somedomain.com", workspace.getApi());
    }

    @Test
    public void test_hasApi_ReturnsFalse_WhenANullApiIsSpecified() {
        workspace.setApi(null);
        assertFalse(workspace.hasApi());
    }

    @Test
    public void test_hasApi_ReturnsFalse_WhenAnEmptyApiIsSpecified() {
        workspace.setApi(" ");
        assertFalse(workspace.hasApi());
    }

    @Test
    public void test_hasApi_ReturnsTrue_WhenAUrlIsSpecified() {
        workspace.setApi("https://www.somedomain.com");
        assertTrue(workspace.hasApi());
    }

    @Test
    public void test_isEmpty_ReturnsTrue_WhenThereAreNoElementsViewsOrDocumentation() {
        workspace = new Workspace("Name", "Description");
        assertTrue(workspace.isEmpty());
    }

    @Test
    public void test_isEmpty_ReturnsFalse_WhenThereAreElements() {
        workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("Name", "Description");
        assertFalse(workspace.isEmpty());
    }

    @Test
    public void test_isEmpty_ReturnsFalse_WhenThereAreViews() {
        workspace = new Workspace("Name", "Description");
        workspace.getViews().createEnterpriseContextView("key", "Description");
        assertFalse(workspace.isEmpty());
    }

    @Test
    public void test_isEmpty_ReturnsFalse_WhenThereIsDocumentation() throws Exception {
        workspace = new Workspace("Name", "Description");
        workspace.getDocumentation().addImages(new File("../docs/images"));
        assertFalse(workspace.isEmpty());
    }

}
