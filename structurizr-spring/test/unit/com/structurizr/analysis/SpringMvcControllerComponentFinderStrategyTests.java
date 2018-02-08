package com.structurizr.analysis;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpringMvcControllerComponentFinderStrategyTests {

    @Test
    public void test_findComponents_FindsSpringMvcControllers() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.SpringMvcControllerComponentFinderStrategy",
                new SpringMvcControllerComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(1, container.getComponents().size());
        Component component = container.getComponentWithName("SomeController");
        assertEquals("test.SpringMvcControllerComponentFinderStrategy.SomeController", component.getPrimaryCode().getType());
        assertEquals("", component.getDescription());
        assertEquals("Spring MVC Controller", component.getTechnology());
    }

}
