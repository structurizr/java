package com.structurizr.analysis;

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
                "com.structurizr.analysis.myapp",
                new SpringComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(5, webApplication.getComponents().size());
        assertEquals(0, webApplication.getComponents().stream().filter(c -> "SomeNonPublicRepository".equals(c.getName())).count());

        Component someMvcController = webApplication.getComponentWithName("SomeController");
        assertNotNull(someMvcController);
        assertEquals("SomeController", someMvcController.getName());
        assertEquals("com.structurizr.analysis.myapp.web.SomeController", someMvcController.getType());
        assertEquals(1, someMvcController.getCode().size());

        Component someRestController = webApplication.getComponentWithName("SomeApiController");
        assertNotNull(someRestController);
        assertEquals("SomeApiController", someRestController.getName());
        assertEquals("com.structurizr.analysis.myapp.api.SomeApiController", someRestController.getType());
        assertEquals(1, someRestController.getCode().size());

        Component someService = webApplication.getComponentWithName("SomeService");
        assertNotNull(someService);
        assertEquals("SomeService", someService.getName());
        assertEquals("com.structurizr.analysis.myapp.service.SomeService", someService.getType());
        assertEquals(2, someService.getCode().size());
        assertCodeElementInComponent(someService, "com.structurizr.analysis.myapp.service.SomeService", CodeElementRole.Primary);
        assertCodeElementInComponent(someService, "com.structurizr.analysis.myapp.service.SomeServiceImpl", CodeElementRole.Supporting);

        Component someRepository = webApplication.getComponentWithName("SomeRepository");
        assertNotNull(someRepository);
        assertEquals("SomeRepository", someRepository.getName());
        assertEquals("com.structurizr.analysis.myapp.data.SomeRepository", someRepository.getType());
        assertEquals(2, someRepository.getCode().size());
        assertCodeElementInComponent(someService, "com.structurizr.analysis.myapp.data.SomeRepository", CodeElementRole.Primary);
        assertCodeElementInComponent(someService, "com.structurizr.analysis.myapp.data.JdbcSomeRepository", CodeElementRole.Supporting);


        Component someOtherRepository = webApplication.getComponentWithName("SomeOtherRepository");
        assertNotNull(someOtherRepository);
        assertEquals("SomeOtherRepository", someOtherRepository.getName());
        assertEquals("com.structurizr.analysis.myapp.data.SomeOtherRepository", someOtherRepository.getType());

        assertEquals(1, someMvcController.getRelationships().size());
        Relationship relationship = someMvcController.getRelationships().iterator().next();
        assertEquals(someMvcController, relationship.getSource());
        assertEquals(someService, relationship.getDestination());

        assertEquals(1, someRestController.getRelationships().size());
        relationship = someRestController.getRelationships().iterator().next();
        assertEquals(someRestController, relationship.getSource());
        assertEquals(someService, relationship.getDestination());

        assertEquals(2, someService.getRelationships().size());

        Set<Relationship> relationships = someService.getRelationships();
        assertNotNull(relationships.stream().filter(r -> r.getDestination() == someRepository).findFirst().get());
        assertNotNull(relationships.stream().filter(r -> r.getDestination() == someOtherRepository).findFirst().get());
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
