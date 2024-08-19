package com.structurizr.component.example;

import com.structurizr.Workspace;
import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.component.ComponentFinderStrategyBuilder;
import com.structurizr.component.filter.ExcludeTypesByRegexFilter;
import com.structurizr.component.matcher.NameSuffixTypeMatcher;
import com.structurizr.component.matcher.RegexTypeMatcher;
import com.structurizr.component.naming.SimpleNamingStrategy;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExampleTests {

    @Test
    void run() {
        Workspace workspace = new Workspace("Name", "Description");

        Person user = workspace.getModel().addPerson("User");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container webApplication = softwareSystem.addContainer("Web Application");
        Container databaseSchema = softwareSystem.addContainer("Database Schema");

        new ComponentFinderBuilder()
                .forContainer(webApplication)
                .fromClasses("build/classes/java/test")
                .fromSource("src/test/java")
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new NameSuffixTypeMatcher("Controller", "Web MVC Controller"))
                                .forEach(component -> user.uses(component, "Uses"))
                                .build()
                )
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new RegexTypeMatcher(".*\\..*Repository", "Data Repository"))
                                .filteredBy(new ExcludeTypesByRegexFilter(".*Ignored.*"))
                                .forEach(component -> component.uses(databaseSchema, "Reads from and writes to"))
                                .namedBy(new SimpleNamingStrategy())
                                .build()
                )
                .build().findComponents();

        assertEquals(2, webApplication.getComponents().size());

        Component controller = webApplication.getComponentWithName("Customer Controller");
        assertNotNull(controller);
        assertEquals("Allows users to view a list of customers.", controller.getDescription());
        assertEquals("Web MVC Controller", controller.getTechnology());
        assertEquals("com.structurizr.component.example.controller.CustomerController", controller.getProperties().get("component.type"));
        assertTrue(controller.getProperties().get("component.src").endsWith("/src/test/java/com/structurizr/component/example/controller/CustomerController.java"));

        Component repository = webApplication.getComponentWithName("CustomerRepository");
        assertNotNull(repository);
        assertEquals("Provides a way to access customer data.", repository.getDescription());
        assertEquals("Data Repository", repository.getTechnology());
        assertEquals("com.structurizr.component.example.repository.CustomerRepository", repository.getProperties().get("component.type"));
        assertTrue(repository.getProperties().get("component.src").endsWith("/src/test/java/com/structurizr/component/example/repository/CustomerRepository.java"));

        assertEquals(1, controller.getRelationships().size());
        assertNotNull(controller.getEfferentRelationshipWith(repository));

        assertNotNull(user.getEfferentRelationshipWith(controller));
        assertNotNull(repository.getEfferentRelationshipWith(databaseSchema));
    }

}