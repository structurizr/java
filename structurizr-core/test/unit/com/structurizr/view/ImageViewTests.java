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

}