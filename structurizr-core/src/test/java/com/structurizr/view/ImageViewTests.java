package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImageViewTests extends AbstractWorkspaceTestBase {

    @Test
    void construction_WhenNoElementIsSpecified() {
        ImageView view = views.createImageView("key");

        assertEquals("key", view.getKey());
        assertNull(view.getElement());
        assertNull(view.getElementId());
    }

    @Test
    void construction_WhenAnElementIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        ImageView view = views.createImageView(softwareSystem, "key");

        assertEquals("key", view.getKey());
        assertSame(softwareSystem, view.getElement());
        assertEquals(softwareSystem.getId(), view.getElementId());
    }

    @Test
    void hasContent_WhenNoContent() {
        ImageView view = views.createImageView("key");
        assertFalse(view.hasContent());
    }

    @Test
    void hasContent_WhenContent() {
        ImageView view = views.createImageView("key");
        view.setContent("https://example.com/image.png");
        assertTrue(view.hasContent());
    }

    @Test
    void hasContent_WhenContentLight() {
        ImageView view = views.createImageView("key");
        view.setContent("https://example.com/image.png", ColorScheme.Light);
        assertTrue(view.hasContent());
    }

    @Test
    void hasContent_WhenContentDark() {
        ImageView view = views.createImageView("key");
        view.setContent("https://example.com/image.png", ColorScheme.Dark);
        assertTrue(view.hasContent());
    }

}