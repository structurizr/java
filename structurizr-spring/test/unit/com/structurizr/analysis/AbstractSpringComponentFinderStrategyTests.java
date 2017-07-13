package com.structurizr.analysis;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractSpringComponentFinderStrategyTests {

    @Test
    public void test_findComponents_IgnoresNonPublicTypesByDefault() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.AbstractSpringComponentFinderStrategy",
                new SpringComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(2, container.getComponents().size());

        Component component = container.getComponentWithName("SomeController");
        assertEquals("test.AbstractSpringComponentFinderStrategy.SomeController", component.getType());

        component = container.getComponentWithName("SomePublicRepository");
        assertEquals("test.AbstractSpringComponentFinderStrategy.SomePublicRepository", component.getType());
    }

    @Test
    public void test_findComponents_DoesNotIgnoreNonPublicTypes_WhenConfiguredToIncludeNonPublicTypes() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        SpringComponentFinderStrategy springComponentFinderStrategy = new SpringComponentFinderStrategy();
        springComponentFinderStrategy.setIncludePublicTypesOnly(false);
        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.AbstractSpringComponentFinderStrategy",
                springComponentFinderStrategy
        );
        componentFinder.findComponents();

        assertEquals(3, container.getComponents().size());

        Component component = container.getComponentWithName("SomeController");
        assertEquals("test.AbstractSpringComponentFinderStrategy.SomeController", component.getType());

        component = container.getComponentWithName("SomePublicRepository");
        assertEquals("test.AbstractSpringComponentFinderStrategy.SomePublicRepository", component.getType());

        component = container.getComponentWithName("SomeNonPublicRepository");
        assertEquals("test.AbstractSpringComponentFinderStrategy.SomeNonPublicRepository", component.getType());
    }

}
