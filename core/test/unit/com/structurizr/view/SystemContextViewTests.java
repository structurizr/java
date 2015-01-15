package com.structurizr.view;

import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class SystemContextViewTests {

    private Model model = new Model("Name", "Description");
    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
    private SystemContextView view;

    @Test
    public void test_construction() {
        view = new SystemContextView(softwareSystem);
        assertEquals(ViewType.SystemContext, view.getType());
        assertEquals("The System - System Context", view.getName());
        assertEquals(1, view.getElements().size());
        assertSame(view.getElements().iterator().next().getElement(), softwareSystem);
        assertSame(softwareSystem, view.getSoftwareSystem());
        assertEquals(softwareSystem.getId(), view.getSoftwareSystemId());
        assertSame(model, view.getModel());
    }

}
