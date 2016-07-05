package com.structurizr.componentfinder;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import static com.structurizr.componentfinder.TestConstants.CONTROLLER_SUFFIX;
import static com.structurizr.componentfinder.TestConstants.MY_APP_TEST_PACKAGE_TO_SCAN;
import static com.structurizr.componentfinder.TestConstants.REPOSITORY_SUFFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        assertEquals(2, webApplication.getComponents().size());

        Component someComponent = webApplication.getComponentWithName("SomeComponent");
        assertNotNull(someComponent);
        assertEquals("SomeComponent", someComponent.getName());
        assertEquals("com.structurizr.componentfinder.subtypes.SomeComponent", someComponent.getType());

        Component loggingComponent = webApplication.getComponentWithName("LoggingComponent");
        assertNotNull(loggingComponent);
        assertEquals("LoggingComponent", loggingComponent.getName());
        assertEquals("com.structurizr.componentfinder.subtypes.LoggingComponent", loggingComponent.getType());

        assertEquals(1, someComponent.getRelationships().size());
        assertNotNull(someComponent.getRelationships().stream().filter(r -> r.getDestination() == loggingComponent).findFirst().get());
    }

    @Test
    public void test_findComponents_CorrectlyFindsDependenciesBetweenComponentsFoundByDifferentComponentFinders_WhenPackage1IsScannedFirst() throws Exception {
        final ComponentFinder componentFinder1 = createControllerFinder();
        final ComponentFinder componentFinder2 = createRepositoryFinder();

        componentFinder1.findComponents();
        componentFinder2.findComponents();

        validateMyAppComponents();
    }

    @Test
    public void test_findComponents_CorrectlyFindsDependenciesBetweenComponentsFoundByDifferentComponentFinders_WhenPackage2IsScannedFirst() throws Exception {
        final ComponentFinder componentFinder1 = createControllerFinder();
        final ComponentFinder componentFinder2 = createRepositoryFinder();

        componentFinder2.findComponents();
        componentFinder1.findComponents();

        validateMyAppComponents();
    }

    private void validateMyAppComponents() {
        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());
    }

    private ComponentFinder createRepositoryFinder() {
        return new ComponentFinder(
                webApplication,
                MY_APP_TEST_PACKAGE_TO_SCAN,
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher(REPOSITORY_SUFFIX, "", "")
                )
        );
    }

    private ComponentFinder createControllerFinder() {
        return new ComponentFinder(
                webApplication,
                MY_APP_TEST_PACKAGE_TO_SCAN,
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher(CONTROLLER_SUFFIX, "", "")
                )
        );
    }

}
