package com.structurizr.componentfinder;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpringComponentFinderStrategyTests {

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
                "com.structurizr.componentfinder.myapp",
                new SpringComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(4, webApplication.getComponents().size());

        Component someController = webApplication.getComponentWithName("SomeController");
        assertNotNull(someController);
        assertEquals("SomeController", someController.getName());
        assertEquals("com.structurizr.componentfinder.myapp.web.SomeController", someController.getType());

        Component someService = webApplication.getComponentWithName("SomeService");
        assertNotNull(someService);
        assertEquals("SomeService", someService.getName());
        assertEquals("com.structurizr.componentfinder.myapp.service.SomeService", someService.getType());

        Component someRepository = webApplication.getComponentWithName("SomeRepository");
        assertNotNull(someRepository);
        assertEquals("SomeRepository", someRepository.getName());
        assertEquals("com.structurizr.componentfinder.myapp.data.SomeRepository", someRepository.getType());

        Component someOtherRepository = webApplication.getComponentWithName("SomeOtherRepository");
        assertNotNull(someOtherRepository);
        assertEquals("SomeOtherRepository", someOtherRepository.getName());
        assertEquals("com.structurizr.componentfinder.myapp.data.SomeOtherRepository", someOtherRepository.getType());

        assertEquals(1, someController.getRelationships().size());
        Relationship relationship = someController.getRelationships().iterator().next();
        assertEquals(someController, relationship.getSource());
        assertEquals(someService, relationship.getDestination());

        assertEquals(2, someService.getRelationships().size());

        Set<Relationship> relationships = someService.getRelationships();
        assertNotNull(relationships.stream().filter(r -> r.getDestination() == someRepository).findFirst().get());
        assertNotNull(relationships.stream().filter(r -> r.getDestination() == someOtherRepository).findFirst().get());
    }

}
