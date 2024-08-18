package com.structurizr.component.example;

import com.structurizr.Workspace;
import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.component.ComponentFinderStrategyBuilder;
import com.structurizr.component.matcher.NameSuffixTypeMatcher;
import com.structurizr.component.naming.CommonsLangCamelCaseNamingStrategy;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

public class Example {

    public static void main(String[] args) {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");

        new ComponentFinderBuilder()
                .forContainer(container)
                .fromClasses("structurizr-component/build/classes/java/test")
                .fromSource("structurizr-component/src/test/java")
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new NameSuffixTypeMatcher("Controller", "Web MVC Controller"))
                                .namedBy(new CommonsLangCamelCaseNamingStrategy())
                                .build()
                )
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new NameSuffixTypeMatcher("Repository", "Data Repository"))
                                .namedBy(new CommonsLangCamelCaseNamingStrategy())
                                .build()
                )
                .build().findComponents();

        for (Component component : container.getComponents()) {
            System.out.println(component.getName());
            System.out.println(" - Description: " + component.getDescription());
            System.out.println(" - Technology: " + component.getTechnology());
            System.out.println(" - Type: " + component.getProperties().get("component.type"));
            System.out.println(" - Source: " + component.getProperties().get("component.src"));
            for (Relationship relationship : component.getRelationships()) {
                System.out.println(" -> " + relationship.getDestination().getName());
            }
        }
    }

}
