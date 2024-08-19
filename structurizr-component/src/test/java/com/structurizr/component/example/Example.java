package com.structurizr.component.example;

import com.structurizr.Workspace;
import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.component.ComponentFinderStrategyBuilder;
import com.structurizr.component.matcher.NameSuffixTypeMatcher;
import com.structurizr.model.*;

public class Example {

    public static void main(String[] args) {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());

        Person user = workspace.getModel().addPerson("User");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container webApplication = softwareSystem.addContainer("Web Application");
        Container databaseSchema = softwareSystem.addContainer("Database Schema");

        new ComponentFinderBuilder()
                .forContainer(webApplication)
                .fromClasses("structurizr-component/build/classes/java/test")
                .fromSource("structurizr-component/src/test/java")
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new NameSuffixTypeMatcher("Controller", "Web MVC Controller"))
                                .forEach(component -> user.uses(component, "Uses"))
                                .build()
                )
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new NameSuffixTypeMatcher("Repository", "Data Repository"))
                                .forEach(component -> component.uses(databaseSchema, "Reads from and writes to"))
                                .build()
                )
                .build().findComponents();

        for (Element element : workspace.getModel().getElements()) {
            print(element);
        }
    }

    private static void print(Element element) {
        System.out.println("[" + element.getClass().getSimpleName() + "] " + element.getName());
        System.out.println(" - Description: " + element.getDescription());

        if (element instanceof Component component) {
            System.out.println(" - Technology: " + component.getTechnology());
            System.out.println(" - Type: " + element.getProperties().get("component.type"));
            System.out.println(" - Source: " + element.getProperties().get("component.src"));
        }

        for (Relationship relationship : element.getRelationships()) {
            System.out.println(" -> [" + relationship.getDestination().getClass().getSimpleName() + "] " + relationship.getDestination().getName());
        }
    }

}
