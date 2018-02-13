package com.structurizr.analysis;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpringRepositoryComponentFinderStrategyTests {

    @Test
    public void test_findComponents_FindsSpringRepositoriesDefinedWithAnAnnotation() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.SpringRepositoryComponentFinderStrategy.annotation",
                new SpringRepositoryComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(1, container.getComponents().size());
        Component component = container.getComponentWithName("SomeRepository");
        assertEquals("test.SpringRepositoryComponentFinderStrategy.annotation.SomeRepository", component.getPrimaryCode().getType());
        assertEquals("", component.getDescription());
        assertEquals("Spring Repository", component.getTechnology());
    }

    @Test
    public void test_findComponents_FindsSpringRepositoriesThatExtendJpaRepository() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.SpringRepositoryComponentFinderStrategy.jpaRepository",
                new SpringRepositoryComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(1, container.getComponents().size());
        Component component = container.getComponentWithName("SomeJpaRepository");
        assertEquals("test.SpringRepositoryComponentFinderStrategy.jpaRepository.SomeJpaRepository", component.getPrimaryCode().getType());
        assertEquals("", component.getDescription());
        assertEquals("Spring Repository", component.getTechnology());
    }

    @Test
    public void test_findComponents_FindsSpringRepositoriesThatExtendCrudRepository() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.SpringRepositoryComponentFinderStrategy.crudRepository",
                new SpringRepositoryComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(1, container.getComponents().size());
        Component component = container.getComponentWithName("SomeCrudRepository");
        assertEquals("test.SpringRepositoryComponentFinderStrategy.crudRepository.SomeCrudRepository", component.getPrimaryCode().getType());
        assertEquals("", component.getDescription());
        assertEquals("Spring Repository", component.getTechnology());
    }

}
