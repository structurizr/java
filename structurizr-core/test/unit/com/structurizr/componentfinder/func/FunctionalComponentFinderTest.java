package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.structurizr.componentfinder.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalComponentFinderTest {

    private static final Predicate<Class<?>> IS_INTERFACE = Class::isInterface;
    private Container container;

    @Before
    public void setUp() {
        container = createDefaultContainer();
    }

    @Test
    public void findComponentsInMyApp() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithDirectDependencies(MATCH_ALL_TYPES_REGEX);
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        new DirectDependenciesValidator(container).validateMyAppComponentDependencies(components);
    }

    @Test
    public void findComponentsInPaperboy() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithDirectDependencies(".*");//TODO scan for sub packages
        final Collection<Component> components = componentFinder.findComponents(PAPERBOY_APP_PACKAGE_TO_SCAN);
        new DirectDependenciesValidator(container).validatePaperBoyComponentDependencies(components);
    }

    @Test
    public void combineMultipleComponentsFinder() throws Exception {
        final FunctionalComponentFinder componentFinder = FunctionalComponentFinder.builder()
                .addComponentFactory(createComponentFactoryWithTag(".*\\.paperboy\\.model", ".*Factory", "FACTORY"))
                .addComponentFactory(createComponentFactoryWithTag(".*\\.paperboy\\.service", ".*Service", "SERVICE"))
                .addComponentFactory(createComponentFactoryWithTag(".*\\.paperboy.*PapersService", ".*Service", "SERVICE"))
                .addComponentFactory(createComponentFactoryWithTag(".*\\.paperboy.*ApplicationService", ".*Service", "SERVICE"))
                .withDirectDependencyScanner()
                .build();
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        new DirectDependenciesValidator(container).validateMyAppComponentDependencies(components);
    }

    @Test
    public void matchOnlyOneComponent() throws Exception {
        final FunctionalComponentFinder componentFinder = FunctionalComponentFinder.builder()
                .addComponentFactory(createComponentFactory(".*\\.myapp\\.*Controller"))
                .withDirectDependencyScanner()
                .build();
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        assertThat(components).hasSize(1);
    }

    @Test
    public void combineComponentsFinderStruct() throws Exception {
        final FunctionalComponentFinder componentFinder = FunctionalComponentFinder.builder()
                .addComponentFactory(createComponentFactoryWithTag(".*\\.paperboy\\.model", ".*Factory", "FACTORY"))
                .addComponentFactory(createComponentFactoryWithTag(".*\\.paperboy\\.service", ".*Service", "SERVICE"))
                .addComponentFactory(createComponentFactoryWithTag(".*\\.paperboy.*PapersService", ".*Service", "SERVICE"))
                .addComponentFactory(createComponentFactoryWithTag(".*\\.paperboy.*ApplicationService", ".*Service", "SERVICE"))
                .withStructurizrDependencyScanner()
                .build();
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        new StructurizrDependenciesValidator(container).validateMyAppComponentDependencies(components);
    }

    @Test
    public void findComponentsInPaperboyWithStructurizrDependencies() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithStructurizrDependencies(".*");//TODO scan for sub packages
        final Collection<Component> components = componentFinder.findComponents(PAPERBOY_APP_PACKAGE_TO_SCAN);
        new StructurizrDependenciesValidator(container).validatePaperBoyComponentDependencies(components);
    }


    private FunctionalComponentFinder createComponentFinderWithDirectDependencies(String typeRegex) {
        return FunctionalComponentFinder.builder()
                .addComponentFactory(createComponentFactory(typeRegex))
                .withDirectDependencyScanner()
                .build();
    }

    private FunctionalComponentFinder createComponentFinderWithStructurizrDependencies(String typeRegex) {
        return FunctionalComponentFinder.builder()
                .addComponentFactory(createComponentFactory(typeRegex))
                .withStructurizrDependencyScanner()
                .build();
    }

    private ComponentFactory createComponentFactory(String typeRegex) {
        final Consumer<CreatedComponent> decorator = createStandardTags().build();
        return createComponentFactory(typeRegex, decorator);
    }

    private ComponentFactory createComponentFactoryWithTag(String componentTypeRegex, String tagRegex, String additionalTag) {
        final Consumer<CreatedComponent> decorator = createStandardTags()
                .addTagForMatchingClass(tagRegex, additionalTag)
                .build();
        return createComponentFactory(componentTypeRegex, decorator);
    }

    private ComponentFactory createComponentFactory(String typeRegex, Consumer<CreatedComponent> decorator) {
        return ComponentFactory.builder()
                .withTypeMatching(typeRegex)
                .withFactoryFromTypeForContainer(container)
                .withDecorator(decorator)
                .build();
    }


    private TagDecorator.Builder createStandardTags() {
        return TagDecorator.builder()
                .addTagForMatchingClass(MATCH_PAPERBOY_PACKAGE_REGEX, PAPERBOY_TAG)
                .addTagForMatchingClass(MATCH_MYAPP_PACKAGE_REGEX, MYAPP_TAG)
                .addTag(IS_INTERFACE, JAVA_INTERFACE)
                .addTag(IS_INTERFACE.negate(), JAVA_CLASS);
    }


}
