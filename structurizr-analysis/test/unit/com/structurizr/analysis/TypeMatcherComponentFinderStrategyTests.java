package com.structurizr.analysis;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeMatcherComponentFinderStrategyTests {

    private Container container;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        container = softwareSystem.addContainer("Name", "Description", "Technology");
    }

    @Test
    public void test_basicUsage() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.TypeMatcherComponentFinderStrategy",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Controller", "Controller description", "Controller technology"),
                        new NameSuffixTypeMatcher("Repository", "Repository description", "Repository technology")
                )
        );
        componentFinder.findComponents();

        assertEquals(2, container.getComponents().size());

        Component myController = container.getComponentWithName("MyController");
        assertNotNull(myController);
        assertEquals("MyController", myController.getName());
        assertEquals("test.TypeMatcherComponentFinderStrategy.MyController", myController.getType().getType());
        assertEquals("Controller description", myController.getDescription());
        assertEquals("Controller technology", myController.getTechnology());

        Component myRepository = container.getComponentWithName("MyRepository");
        assertNotNull(myRepository);
        assertEquals("MyRepository", myRepository.getName());
        assertEquals("test.TypeMatcherComponentFinderStrategy.MyRepository", myRepository.getType().getType());
        assertEquals("Repository description", myRepository.getDescription());
        assertEquals("Repository technology", myRepository.getTechnology());

        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());
    }

}