package com.structurizr.analysis;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpringServiceComponentFinderStrategyTests {

    @Test
    public void test_findComponents_FindsSpringServices() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.SpringServiceComponentFinderStrategy",
                new SpringServiceComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(1, container.getComponents().size());
        Component component = container.getComponentWithName("SomeService");
        assertEquals("test.SpringServiceComponentFinderStrategy.SomeService", component.getType());
        assertEquals("", component.getDescription());
        assertEquals("Spring Service", component.getTechnology());
    }

}
