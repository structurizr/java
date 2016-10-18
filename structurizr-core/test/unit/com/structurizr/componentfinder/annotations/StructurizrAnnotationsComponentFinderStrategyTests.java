package com.structurizr.componentfinder.annotations;

import com.structurizr.Workspace;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.StructurizrAnnotationsComponentFinderStrategy;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class StructurizrAnnotationsComponentFinderStrategyTests {

    private SoftwareSystem externalSoftwareSystem;
    private Person user1;
    private Person user2;
    private Container webBrowser;
    private Container webApplication;
    private Container database;
    private Container logstash;

    @Before
    public void setUp() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        Model model = workspace.getModel();

        externalSoftwareSystem = model.addSoftwareSystem("External software system", "");
        user1 = model.addPerson("User 1", "");
        user2 = model.addPerson("User 2", "");
        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Software System", "");
        webBrowser = mySoftwareSystem.addContainer("Web Browser", "", "");
        webApplication = mySoftwareSystem.addContainer("Name", "", "");
        database = mySoftwareSystem.addContainer("Database", "", "");
        logstash = mySoftwareSystem.addContainer("Logstash", "", "");

        SoftwareSystem someOtherSoftwareSystem = model.addSoftwareSystem("Software System A", "");

        StructurizrAnnotationsComponentFinderStrategy componentFinderStrategy = new StructurizrAnnotationsComponentFinderStrategy();

        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.annotations",
                componentFinderStrategy
        );
        componentFinder.findComponents();
    }

    @Test
    public void test_ThereAreFiveComponents() {
        // there should be 5 components: MyHtmlController, MyJsonController, MyService, MyRepository and the LoggingComponent
        assertEquals(5, webApplication.getComponents().size());

        Component myHtmlController = webApplication.getComponentWithName("MyHtmlController");
        assertNotNull(myHtmlController);
        assertEquals("MyHtmlController", myHtmlController.getName());
        assertEquals("com.structurizr.componentfinder.annotations.controller.html.MyHtmlController", myHtmlController.getType());
        assertEquals("Allows users to do something", myHtmlController.getDescription());
        assertEquals(1, myHtmlController.getCode().size());

        Component myJsonController = webApplication.getComponentWithName("MyJsonController");
        assertNotNull(myJsonController);
        assertEquals("MyJsonController", myJsonController.getName());
        assertEquals("com.structurizr.componentfinder.annotations.controller.json.MyJsonController", myJsonController.getType());
        assertEquals("Allows systems to do something", myJsonController.getDescription());
        assertEquals(1, myJsonController.getCode().size());

        Component myService = webApplication.getComponentWithName("MyService");
        assertNotNull(myService);
        assertEquals("MyService", myService.getName());
        assertEquals("com.structurizr.componentfinder.annotations.service.MyService", myService.getType());
        assertEquals("A component that does some business logic", myService.getDescription());
        assertEquals(2, myService.getCode().size());
        assertCodeElementInComponent(myService, "com.structurizr.componentfinder.annotations.service.MyService", CodeElementRole.Primary);
        assertCodeElementInComponent(myService, "com.structurizr.componentfinder.annotations.service.MyServiceImpl", CodeElementRole.Supporting);

        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertNotNull(myRepository);
        assertEquals("MyRepository", myRepository.getName());
        assertEquals("com.structurizr.componentfinder.annotations.repository.MyRepository", myRepository.getType());
        assertEquals("Manages some data", myRepository.getDescription());
        assertEquals(2, myRepository.getCode().size());
        assertCodeElementInComponent(myRepository, "com.structurizr.componentfinder.annotations.repository.MyRepository", CodeElementRole.Primary);
        assertCodeElementInComponent(myRepository, "com.structurizr.componentfinder.annotations.repository.MyRepositoryImpl", CodeElementRole.Supporting);

        Component loggingComponent = webApplication.getComponentWithName("LoggingComponent");
        assertNotNull(loggingComponent);
        assertEquals("LoggingComponent", loggingComponent.getName());
        assertEquals("com.structurizr.componentfinder.annotations.logging.LoggingComponent", loggingComponent.getType());
        assertEquals("Writes log entries", loggingComponent.getDescription());
        assertEquals("Java and Logstash", loggingComponent.getTechnology());
        assertEquals(2, loggingComponent.getCode().size());
        assertCodeElementInComponent(loggingComponent, "com.structurizr.componentfinder.annotations.logging.LoggingComponent", CodeElementRole.Primary);
        assertCodeElementInComponent(loggingComponent, "com.structurizr.componentfinder.annotations.logging.LoggingComponentImpl", CodeElementRole.Supporting);
    }

    @Test
    public void test_AllComponentsHaveADependencyOnTheLoggingComponent() {
        Component myHtmlController = webApplication.getComponentWithName("MyHtmlController");
        Component myJsonController = webApplication.getComponentWithName("MyJsonController");
        Component myService = webApplication.getComponentWithName("MyService");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        Component loggingComponent = webApplication.getComponentWithName("LoggingComponent");
        Relationship relationship;

        relationship = myHtmlController.getRelationships().stream().filter(r -> r.getDestination() == loggingComponent).findFirst().get();
        assertEquals("Writes log entries using", relationship.getDescription());

        relationship = myJsonController.getRelationships().stream().filter(r -> r.getDestination() == loggingComponent).findFirst().get();
        assertEquals("Writes log entries using", relationship.getDescription());

        relationship = myService.getRelationships().stream().filter(r -> r.getDestination() == loggingComponent).findFirst().get();
        assertEquals("Writes log entries using", relationship.getDescription());

        relationship = myRepository.getRelationships().stream().filter(r -> r.getDestination() == loggingComponent).findFirst().get();
        assertEquals("Writes log entries using", relationship.getDescription());
    }

    @Test
    public void test_LoggingComponentUsesTheLogstashContainer()
    {
        Component loggingComponent = webApplication.getComponentWithName("LoggingComponent");

        Relationship relationship = loggingComponent.getRelationships().stream().filter(r -> r.getDestination() == logstash).findFirst().get();
        assertEquals("Writes log entries to", relationship.getDescription());
    }

    @Test
    public void test_MyHtmlControllerUsesMyService()
    {
        Component myHtmlController = webApplication.getComponentWithName("MyHtmlController");
        Component myService = webApplication.getComponentWithName("MyService");

        Relationship relationship = myHtmlController.getRelationships().stream().filter(r -> r.getDestination() == myService).findFirst().get();
        assertEquals("Delegates business logic to", relationship.getDescription());
    }

    @Test
    public void test_MyJsonControllerUsesMyService()
    {
        Component myJsonController = webApplication.getComponentWithName("MyJsonController");
        Component myService = webApplication.getComponentWithName("MyService");

        Relationship relationship = myJsonController.getRelationships().stream().filter(r -> r.getDestination() == myService).findFirst().get();
        assertEquals("Delegates business logic to", relationship.getDescription());
    }

    @Test
    public void test_MyServiceUsesMyRepository()
    {
        Component myService = webApplication.getComponentWithName("MyService");
        Component myRepository = webApplication.getComponentWithName("MyRepository");

        Relationship relationship = myService.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get();
        assertEquals("Uses", relationship.getDescription());
    }

    @Test
    public void test_MyServiceUsesTheExternalSoftwareSystem()
    {
        Component myService = webApplication.getComponentWithName("MyService");

        Relationship relationship = myService.getRelationships().stream().filter(r -> r.getDestination() == externalSoftwareSystem).findFirst().get();
        assertEquals("Sends something to", relationship.getDescription());
    }

    @Test
    public void test_MyRepositoryUsesTheDatabase()
    {
        Component myRepository = webApplication.getComponentWithName("MyRepository");

        Relationship relationship = myRepository.getRelationships().stream().filter(r -> r.getDestination() == database).findFirst().get();
        assertEquals("Reads from and writes to", relationship.getDescription());
    }

    @Test
    public void test_MyHtmlControllerIsUsedByUser1() {
        Component myHtmlController = webApplication.getComponentWithName("MyHtmlController");

        Relationship relationship = user1.getRelationships().stream().filter(r -> r.getDestination() == myHtmlController).findFirst().get();
        assertEquals("Uses to do something", relationship.getDescription());
    }

    @Test
    public void test_MyHtmlControllerIsUsedByUser2() {
        Component myHtmlController = webApplication.getComponentWithName("MyHtmlController");

        Relationship relationship = user2.getRelationships().stream().filter(r -> r.getDestination() == myHtmlController).findFirst().get();
        assertEquals("Uses to do something too", relationship.getDescription());
    }

    @Test
    public void test_TheExternalSoftwareSystemUsesTheJsonController() {
        Component myJsonController = webApplication.getComponentWithName("MyJsonController");

        Relationship relationship = externalSoftwareSystem.getRelationships().stream().filter(r -> r.getDestination() == myJsonController).findFirst().get();
        assertEquals("Uses to do something", relationship.getDescription());
    }

    @Test
    public void test_TheWebBrowserSystemUsesTheJsonController() {
        Component myJsonController = webApplication.getComponentWithName("MyJsonController");

        Relationship relationship = webBrowser.getRelationships().stream().filter(r -> r.getDestination() == myJsonController).findFirst().get();
        assertEquals("Does something via the API", relationship.getDescription());
    }

    private boolean assertCodeElementInComponent(Component component, String type, CodeElementRole role) {
        for (CodeElement codeElement : component.getCode()) {
            if (codeElement.getType().equals(type)) {
                return codeElement.getRole() == role;
            }
        }

        return false;
    }

}
