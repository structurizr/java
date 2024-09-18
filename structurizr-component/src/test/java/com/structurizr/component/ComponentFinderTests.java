package com.structurizr.component;

import com.structurizr.Workspace;
import com.structurizr.component.description.FirstSentenceDescriptionStrategy;
import com.structurizr.component.filter.IncludeFullyQualifiedNameRegexFilter;
import com.structurizr.component.matcher.ImplementsTypeMatcher;
import com.structurizr.component.naming.TypeNamingStrategy;
import com.structurizr.component.url.PrefixSourceUrlStrategy;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.github.javaparser.utils.Utils.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComponentFinderTests {

    @Test
    void run() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        Container container = softwareSystem.addContainer("Name");

        ComponentFinder componentFinder = new ComponentFinderBuilder()
                .forContainer(container)
                .fromClasses(new File("build/classes/java/test"))
                .fromSource(new File("src/test/java"))
                .filteredBy(new IncludeFullyQualifiedNameRegexFilter("com\\.structurizr\\.component\\.example\\..*"))
                .withStrategy(new ComponentFinderStrategyBuilder()
                        .withTechnology("Web Controller")
                        .matchedBy(new ImplementsTypeMatcher("com.structurizr.component.example.Controller"))
                        .filteredBy(new IncludeFullyQualifiedNameRegexFilter("com\\.structurizr\\.component\\.example\\..*"))
                        .withName(new TypeNamingStrategy())
                        .withDescription(new FirstSentenceDescriptionStrategy())
                        .withUrl(new PrefixSourceUrlStrategy("https://example.com/src/main/java"))
                        .build()
                )
                .withStrategy(new ComponentFinderStrategyBuilder()
                        .withTechnology("Data Repository")
                        .matchedBy(new ImplementsTypeMatcher("com.structurizr.component.example.Repository"))
                        .filteredBy(new IncludeFullyQualifiedNameRegexFilter("com\\.structurizr\\.component\\.example\\..*"))
                        .withName(new TypeNamingStrategy())
                        .withDescription(new FirstSentenceDescriptionStrategy())
                        .withUrl(new PrefixSourceUrlStrategy("https://example.com/src/main/java"))
                        .build()
                )
                .build();

        componentFinder.run();

        assertEquals(2, container.getComponents().size());
        Component exampleController = container.getComponentWithName("ExampleController");
        assertNotNull(exampleController);
        assertEquals("https://example.com/src/main/java/com/structurizr/component/example/ExampleController.java", exampleController.getUrl());
        assertTrue(exampleController.hasTag("Controller"));
        assertEquals("https://example.com", exampleController.getProperties().get("Documentation"));

        Component exampleRepository = container.getComponentWithName("ExampleRepository");
        assertNotNull(exampleRepository);
        assertEquals("https://example.com/src/main/java/com/structurizr/component/example/ExampleRepository.java", exampleRepository.getUrl());

        assertTrue(exampleController.hasEfferentRelationshipWith(exampleRepository));
    }

}
