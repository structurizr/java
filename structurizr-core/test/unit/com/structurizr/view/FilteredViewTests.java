package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FilteredViewTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_construction() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        SystemContextView systemContextView = views.createSystemContextView(softwareSystem, "SystemContext", "Description");
        FilteredView filteredView = views.createFilteredView(
                systemContextView,
                "CurrentStateSystemContext",
                "The system context as-is.",
                FilterMode.Exclude,
                "v2", "v3"
        );

        assertEquals("CurrentStateSystemContext", filteredView.getKey());
        assertEquals("SystemContext", filteredView.getBaseViewKey());
        assertSame(systemContextView, filteredView.getView());
        assertEquals("The system context as-is.", filteredView.getDescription());
        assertEquals(FilterMode.Exclude, filteredView.getMode());
        assertEquals(2, filteredView.getTags().size());
        assertTrue(filteredView.getTags().contains("v2"));
        assertTrue(filteredView.getTags().contains("v3"));
    }

}