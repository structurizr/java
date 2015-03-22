package com.structurizr.componentfinder.annotations;

import com.structurizr.Workspace;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.JavadocComponentFinderStrategy;
import com.structurizr.componentfinder.StructurizrAnnotationsComponentFinderStrategy;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StructurizrAnnotationsComponentFinderStrategyTests {

    private Container webApplication;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        webApplication = softwareSystem.addContainer("Name", "Description", "Technology");
    }

    @Test
    public void test_findComponents() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.annotations",
                new StructurizrAnnotationsComponentFinderStrategy()
        );
        componentFinder.findComponents();

        Component componentA = webApplication.getComponentWithName("ComponentA");
        assertNotNull(componentA);
        assertEquals("ComponentA", componentA.getName());
        assertEquals("com.structurizr.componentfinder.annotations.componentA.ComponentA", componentA.getInterfaceType());
        assertEquals("com.structurizr.componentfinder.annotations.componentA.ComponentAImpl", componentA.getImplementationType());
        assertEquals("A component that does something.", componentA.getDescription());

        Component componentB = webApplication.getComponentWithName("ComponentB");
        assertNotNull(componentB);
        assertEquals("ComponentB", componentB.getName());
        assertEquals("com.structurizr.componentfinder.annotations.componentB.ComponentB", componentB.getInterfaceType());
        assertEquals("com.structurizr.componentfinder.annotations.componentB.ComponentBImpl", componentB.getImplementationType());
        assertEquals("A component that does something else.", componentB.getDescription());

        assertEquals(1, componentB.getRelationships().size());

        Relationship relationship = componentB.getRelationships().iterator().next();
        assertEquals(componentB, relationship.getSource());
        assertEquals(componentA, relationship.getDestination());
        assertEquals("Does something with", relationship.getDescription());
    }

}
