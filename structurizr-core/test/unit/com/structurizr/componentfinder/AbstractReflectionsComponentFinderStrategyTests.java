package com.structurizr.componentfinder;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AbstractReflectionsComponentFinderStrategyTests {

    private Container webApplication;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        webApplication = softwareSystem.addContainer("Name", "Description", "Technology");
    }

    @Test
    public void test_findComponents_DoesNotBreak_WhenThereIsACyclicDependency() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.cyclic",
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Component", "", "")
                )
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());

        Component aComponent = webApplication.getComponentWithName("AComponent");
        assertNotNull(aComponent);
        assertEquals("AComponent", aComponent.getName());
        assertEquals("com.structurizr.componentfinder.cyclic.AComponent", aComponent.getType());

        Component bComponent = webApplication.getComponentWithName("BComponent");
        assertNotNull(bComponent);
        assertEquals("BComponent", bComponent.getName());
        assertEquals("com.structurizr.componentfinder.cyclic.BComponent", bComponent.getType());

        assertEquals(1, aComponent.getRelationships().size());
        assertNotNull(aComponent.getRelationships().stream().filter(r -> r.getDestination() == bComponent).findFirst().get());

        assertEquals(1, bComponent.getRelationships().size());
        assertNotNull(bComponent.getRelationships().stream().filter(r -> r.getDestination() == aComponent).findFirst().get());
    }

    @Test
    public void test_findComponents_CorrectlyFindsDependenciesFromSuperclass() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.subtypes",
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Component", "", "")
                )
        );
        componentFinder.findComponents();

        assertEquals(3, webApplication.getComponents().size());

        Component someComponent = webApplication.getComponentWithName("SomeComponent");
        assertNotNull(someComponent);
        assertEquals("SomeComponent", someComponent.getName());
        assertEquals("com.structurizr.componentfinder.subtypes.SomeComponent", someComponent.getType());

        Component otherComponent = webApplication.getComponentWithName("OtherComponent");
        assertNotNull(otherComponent);
        assertEquals("OtherComponent", otherComponent.getName());
        assertEquals("com.structurizr.componentfinder.subtypes.OtherComponent", otherComponent.getType());

        Component loggingComponent = webApplication.getComponentWithName("LoggingComponent");
        assertNotNull(loggingComponent);
        assertEquals("LoggingComponent", loggingComponent.getName());
        assertEquals("com.structurizr.componentfinder.subtypes.LoggingComponent", loggingComponent.getType());

        assertEquals(1, someComponent.getRelationships().size());
        assertNotNull(someComponent.getRelationships().stream().filter(r -> r.getDestination() == loggingComponent).findFirst().get());
        assertNull(someComponent.getRelationships().stream().filter(r -> r.getDestination() == otherComponent).findFirst().orElse(null));
        assertNull(otherComponent.getRelationships().stream().filter(r -> r.getDestination() == someComponent).findFirst().orElse(null));
    }

    @Test
    public void test_findComponents_CorrectlyFindsDependenciesBetweenComponentsFoundByDifferentComponentFinders_WhenPackage1IsScannedFirst() throws Exception {
        ComponentFinder componentFinder1 = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.packages.package1",
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Controller", "", "")
                )
        );

        ComponentFinder componentFinder2 = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.packages.package2",
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Repository", "", "")
                )
        );

        componentFinder1.findComponents();
        componentFinder2.findComponents();

        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());
    }

    @Test
    public void test_findComponents_CorrectlyFindsDependenciesBetweenComponentsFoundByDifferentComponentFinders_WhenPackage2IsScannedFirst() throws Exception {
        ComponentFinder componentFinder1 = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.packages.package1",
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Controller", "", "")
                )
        );

        ComponentFinder componentFinder2 = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.packages.package2",
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Repository", "", "")
                )
        );

        componentFinder2.findComponents();
        componentFinder1.findComponents();

        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());
    }

}
