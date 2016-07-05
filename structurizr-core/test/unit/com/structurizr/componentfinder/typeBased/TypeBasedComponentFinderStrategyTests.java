package com.structurizr.componentfinder.typeBased;


import com.structurizr.Workspace;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.NameSuffixTypeMatcher;
import com.structurizr.componentfinder.TypeBasedComponentFinderStrategy;
import com.structurizr.componentfinder.reflections.multipleComponentFinders.package1.MyController;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import org.junit.Before;
import org.junit.Test;

import static com.structurizr.componentfinder.ComponentFinderTestConstants.createDefaultContainer;
import static com.structurizr.componentfinder.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeBasedComponentFinderStrategyTests {


    private Container webApplication;

    @Before
    public void setUp() {
        webApplication = createDefaultContainer();
    }

    @Test
    public void test_findComponents() throws Exception {
        TypeBasedComponentFinderStrategy typeBasedComponentFinderStrategy = new TypeBasedComponentFinderStrategy(
                new NameSuffixTypeMatcher("Controller", "", "An MVC controller"),
                new NameSuffixTypeMatcher("Repository", "", "A data access object")
        );

        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.typeBased.myapp",
                typeBasedComponentFinderStrategy
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());

        final Component myController = webApplication.getComponentWithName("MyController");
        assertNotNull(myController);
        assertEquals("MyController", myController.getName());
        assertEquals("com.structurizr.componentfinder.typeBased.myapp.MyController", myController.getType());
        assertEquals("", myController.getDescription());

        final Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertNotNull(myRepository);
        assertEquals("MyRepository", myRepository.getName());
        assertEquals("com.structurizr.componentfinder.typeBased.myapp.MyRepository", myRepository.getType());
        assertEquals("", myRepository.getDescription());

        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());
    }

}
