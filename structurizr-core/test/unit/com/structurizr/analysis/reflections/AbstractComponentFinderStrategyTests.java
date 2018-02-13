package com.structurizr.analysis.reflections;

import com.structurizr.Workspace;
import com.structurizr.analysis.*;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AbstractComponentFinderStrategyTests {

    private Container webApplication;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        webApplication = softwareSystem.addContainer("Name", "Description", "Technology");
    }

    @Test
    public void test_findComponents_DoesNotBreak_WhenThereIsACyclicDependency() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.cyclicDependency",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Component", "", "")
                )
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());

        Component aComponent = webApplication.getComponentWithName("AComponent");
        assertNotNull(aComponent);
        assertEquals("AComponent", aComponent.getName());
        assertEquals("com.structurizr.analysis.reflections.cyclicDependency.AComponent", aComponent.getPrimaryCode().getType());

        Component bComponent = webApplication.getComponentWithName("BComponent");
        assertNotNull(bComponent);
        assertEquals("BComponent", bComponent.getName());
        assertEquals("com.structurizr.analysis.reflections.cyclicDependency.BComponent", bComponent.getPrimaryCode().getType());

        assertEquals(1, aComponent.getRelationships().size());
        assertNotNull(aComponent.getRelationships().stream().filter(r -> r.getDestination() == bComponent).findFirst().get());

        assertEquals(1, bComponent.getRelationships().size());
        assertNotNull(bComponent.getRelationships().stream().filter(r -> r.getDestination() == aComponent).findFirst().get());
    }

    @Test
    public void test_findComponents_CorrectlyFindsDependenciesFromSuperclass() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.dependenciesFromSuperClass",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Component", "", "")
                )
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());

        Component someComponent = webApplication.getComponentWithName("SomeComponent");
        assertNotNull(someComponent);
        assertEquals("SomeComponent", someComponent.getName());
        assertEquals("com.structurizr.analysis.reflections.dependenciesFromSuperClass.SomeComponent", someComponent.getPrimaryCode().getType());

        Component loggingComponent = webApplication.getComponentWithName("LoggingComponent");
        assertNotNull(loggingComponent);
        assertEquals("LoggingComponent", loggingComponent.getName());
        assertEquals("com.structurizr.analysis.reflections.dependenciesFromSuperClass.LoggingComponent", loggingComponent.getPrimaryCode().getType());

        assertEquals(1, someComponent.getRelationships().size());
        assertNotNull(someComponent.getRelationships().stream().filter(r -> r.getDestination() == loggingComponent).findFirst().get());
    }

    @Test
    public void test_findComponents_CorrectlyFindsNoDependenciesWhenTwoComponentsImplementTheSameInterface() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.featureinterface",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Component", "", "")
                )
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());

        Component someComponent = webApplication.getComponentWithName("SomeComponent");
        assertNotNull(someComponent);
        assertEquals("SomeComponent", someComponent.getName());
        assertEquals("com.structurizr.analysis.reflections.featureinterface.SomeComponent", someComponent.getPrimaryCode().getType());

        Component otherComponent = webApplication.getComponentWithName("OtherComponent");
        assertNotNull(otherComponent);
        assertEquals("OtherComponent", otherComponent.getName());
        assertEquals("com.structurizr.analysis.reflections.featureinterface.OtherComponent", otherComponent.getPrimaryCode().getType());

        assertEquals(0, someComponent.getRelationships().size());
        assertEquals(0, otherComponent.getRelationships().size());
    }

    @Test
    public void test_findComponents_CorrectlyFindsDependenciesBetweenComponentsFoundByDifferentComponentFinders_WhenPackage1IsScannedFirst() throws Exception {
        ComponentFinder componentFinder1 = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.multipleComponentFinders.package1",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Controller", "", "")
                )
        );

        ComponentFinder componentFinder2 = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.multipleComponentFinders.package2",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Repository", "", "")
                )
        );

        componentFinder1.findComponents();
        componentFinder2.findComponents();

        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());
    }

    @Test
    public void test_findComponents_CorrectlyFindsDependenciesBetweenComponentsFoundByDifferentComponentFinders_WhenPackage2IsScannedFirst() throws Exception {
        ComponentFinder componentFinder1 = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.multipleComponentFinders.package1",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Controller", "", "")
                )
        );

        ComponentFinder componentFinder2 = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.multipleComponentFinders.package2",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Repository", "", "")
                )
        );

        componentFinder2.findComponents();
        componentFinder1.findComponents();

        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());
    }

    @Test
    public void test_findComponents_CorrectlyFindsSupportingTypes_WhenTheDefaultStrategyIsUsed() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.supportingTypes.myapp",
                new StructurizrAnnotationsComponentFinderStrategy()
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());

        // the default strategy for supporting types is to find the first implementation
        // class if the component type is an interface
        assertEquals(1, myController.getCode().size());
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.web.MyController", CodeElementRole.Primary);

        assertEquals(2, myRepository.getCode().size());
        assertCodeElementInComponent(myRepository, "com.structurizr.analysis.reflections.supportingTypes.data.MyRepository", CodeElementRole.Primary);
        assertCodeElementInComponent(myRepository, "com.structurizr.analysis.reflections.supportingTypes.data.MyRepositoryImpl", CodeElementRole.Supporting);
    }

    @Test
    public void test_findComponents_CorrectlyFindsSupportingTypes_WhenTheReferencedTypesInSamePackageSupportingTypesStrategyIsUsed() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.supportingTypes.myapp",
                new StructurizrAnnotationsComponentFinderStrategy(
                        new FirstImplementationOfInterfaceSupportingTypesStrategy(),
                        new ReferencedTypesInSamePackageSupportingTypesStrategy()
                )
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());

        assertEquals(1, myController.getCode().size());
        assertEquals(3, myRepository.getCode().size());
        assertCodeElementInComponent(myRepository, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepository", CodeElementRole.Primary);
        assertCodeElementInComponent(myRepository, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepositoryImpl", CodeElementRole.Supporting);
        assertCodeElementInComponent(myRepository, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepositoryRowMapper", CodeElementRole.Supporting);
    }

    @Test
    public void test_findComponents_CorrectlyFindsSupportingTypes_WhenTheReferencedTypesStrategyIsUsedAndIndirectlyReferencedTypesShouldBeIncluded() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.supportingTypes.myapp",
                new StructurizrAnnotationsComponentFinderStrategy(
                        new FirstImplementationOfInterfaceSupportingTypesStrategy(),
                        new ReferencedTypesSupportingTypesStrategy()
                )
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());

        assertEquals(2, myController.getCode().size());
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.MyController", CodeElementRole.Primary);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.AbstractComponent", CodeElementRole.Supporting);

        assertEquals(5, myRepository.getCode().size());
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepository", CodeElementRole.Primary);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.AbstractComponent", CodeElementRole.Supporting);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepositoryImpl", CodeElementRole.Supporting);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepositoryRowMapper", CodeElementRole.Supporting);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.util.RowMapperHelper", CodeElementRole.Supporting);
    }

    @Test
    public void test_findComponents_CorrectlyFindsSupportingTypes_WhenTheReferencedTypesStrategyIsUsedAndIndirectlyReferencedTypesShouldBeExcluded() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.analysis.reflections.supportingTypes.myapp",
                new StructurizrAnnotationsComponentFinderStrategy(
                        new FirstImplementationOfInterfaceSupportingTypesStrategy(),
                        new ReferencedTypesSupportingTypesStrategy(false)
                )
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());
        Component myController = webApplication.getComponentWithName("MyController");
        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());

        assertEquals(2, myController.getCode().size());
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.MyController", CodeElementRole.Primary);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.AbstractComponent", CodeElementRole.Supporting);

        assertEquals(4, myRepository.getCode().size());
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepository", CodeElementRole.Primary);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.AbstractComponent", CodeElementRole.Supporting);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepositoryImpl", CodeElementRole.Supporting);
        assertCodeElementInComponent(myController, "com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepositoryRowMapper", CodeElementRole.Supporting);
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
