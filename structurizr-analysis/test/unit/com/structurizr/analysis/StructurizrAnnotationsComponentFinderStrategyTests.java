package com.structurizr.analysis;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StructurizrAnnotationsComponentFinderStrategyTests {

    private SoftwareSystem external1, external2, softwareSystem;
    private Person anonymousUser, authenticatedUser;
    private Container webBrowser, apiClient;
    private Container webApplication;
    private Container database;

    @Before
    public void setUp() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        Model model = workspace.getModel();

        external1 = model.addSoftwareSystem("External 1", "");
        external2 = model.addSoftwareSystem("External 2", "");
        anonymousUser = model.addPerson("Anonymous User", "");
        authenticatedUser = model.addPerson("Authenticated User", "");
        softwareSystem = model.addSoftwareSystem("Software System", "");
        webBrowser = softwareSystem.addContainer("Web Browser", "", "");
        apiClient = softwareSystem.addContainer("API Client", "", "");
        webApplication = softwareSystem.addContainer("Name", "", "");
        database = softwareSystem.addContainer("Database", "", "");

        // the default usage of the StructurizrAnnotationsComponentFinderStrategy
        // just has the FirstImplementationOfInterfaceSupportingTypesStrategy included
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "test.StructurizrAnnotationsComponentFinderStrategy",
                new StructurizrAnnotationsComponentFinderStrategy()
        );
        componentFinder.findComponents();
    }

    @Test
    public void test_Component() {
        assertEquals(2, webApplication.getComponents().size());

        Component controller = webApplication.getComponentWithName("Controller");
        assertNotNull(controller);
        assertEquals("Controller", controller.getName());
        assertEquals("test.StructurizrAnnotationsComponentFinderStrategy.Controller", controller.getType().getType());
        assertEquals("Does something.", controller.getDescription());
        assertEquals(1, controller.getCode().size());
        assertCodeElementInComponent(controller, "test.StructurizrAnnotationsComponentFinderStrategy.Controller", CodeElementRole.Primary);

        Component repository = webApplication.getComponentWithName("Repository");
        assertNotNull(repository);
        assertEquals("Repository", repository.getName());
        assertEquals("test.StructurizrAnnotationsComponentFinderStrategy.Repository", repository.getType().getType());
        assertEquals("Manages some data.", repository.getDescription());
        assertEquals(2, repository.getCode().size());
        assertCodeElementInComponent(repository, "test.StructurizrAnnotationsComponentFinderStrategy.Repository", CodeElementRole.Primary);
        assertCodeElementInComponent(repository, "test.StructurizrAnnotationsComponentFinderStrategy.RepositoryImpl", CodeElementRole.Supporting);
    }

    private void assertCodeElementInComponent(Component component, String type, CodeElementRole role) {
        for (CodeElement codeElement : component.getCode()) {
            if (codeElement.getType().equals(type)) {
                if (codeElement.getRole() == role) {
                    return;
                }
            }
        }

        fail();
    }

    @Test
    public void test_UsedByPerson() {
        Component controller = webApplication.getComponentWithName("Controller");

        assertEquals(1, anonymousUser.getRelationships().size());
        Relationship relationship = anonymousUser.getRelationships().stream().filter(r -> r.getDestination() == controller).findFirst().get();
        assertEquals("Uses to do something", relationship.getDescription());
        assertEquals("HTTPS", relationship.getTechnology());

        assertEquals(1, authenticatedUser.getRelationships().size());
        relationship = authenticatedUser.getRelationships().stream().filter(r -> r.getDestination() == controller).findFirst().get();
        assertEquals("Uses to do something too", relationship.getDescription());
        assertEquals("", relationship.getTechnology());
    }

    @Test
    public void test_UsedBySoftwareSystem() {
        Component controller = webApplication.getComponentWithName("Controller");

        assertEquals(1, external1.getRelationships().size());
        Relationship relationship = external1.getRelationships().stream().filter(r -> r.getDestination() == controller).findFirst().get();
        assertEquals("Uses to do something", relationship.getDescription());
        assertEquals("HTTPS", relationship.getTechnology());

        assertEquals(1, external2.getRelationships().size());
        relationship = external2.getRelationships().stream().filter(r -> r.getDestination() == controller).findFirst().get();
        assertEquals("Uses to do something too", relationship.getDescription());
        assertEquals("", relationship.getTechnology());
    }

    @Test
    public void test_UsedByContainer() {
        Component controller = webApplication.getComponentWithName("Controller");

        assertEquals(1, webBrowser.getRelationships().size());
        Relationship relationship = webBrowser.getRelationships().stream().filter(r -> r.getDestination() == controller).findFirst().get();
        assertEquals("Makes calls to", relationship.getDescription());
        assertEquals("HTTPS", relationship.getTechnology());

        assertEquals(1, apiClient.getRelationships().size());
        relationship = apiClient.getRelationships().stream().filter(r -> r.getDestination() == controller).findFirst().get();
        assertEquals("Makes API calls to", relationship.getDescription());
        assertEquals("HTTPS", relationship.getTechnology());
    }

    @Test
    public void test_UsesComponent()
    {
        Component controller = webApplication.getComponentWithName("Controller");
        Component repository = webApplication.getComponentWithName("Repository");

        Relationship relationship = controller.getRelationships().stream().filter(r -> r.getDestination() == repository).findFirst().get();
        assertEquals("Reads from and writes to", relationship.getDescription());
        assertEquals("Just a method call", relationship.getTechnology());
    }

    @Test
    public void test_UsesContainer()
    {
        Component repository = webApplication.getComponentWithName("Repository");

        assertEquals(1, repository.getRelationships().size());
        Relationship relationship = repository.getRelationships().stream().filter(r -> r.getDestination() == database).findFirst().get();
        assertEquals("Reads from and writes to", relationship.getDescription());
        assertEquals("JDBC", relationship.getTechnology());
    }

    @Test
    public void test_UsesSoftwareSystem()
    {
        Component controller = webApplication.getComponentWithName("Controller");

        Relationship relationship = controller.getRelationships().stream().filter(r -> r.getDestination() == external1).findFirst().get();
        assertEquals("Sends information to", relationship.getDescription());
        assertEquals("HTTPS", relationship.getTechnology());
    }

}