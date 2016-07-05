package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.testapp.paperboy.model.Customer;
import com.structurizr.testapp.paperboy.model.CustomerFactory;
import com.structurizr.testapp.paperboy.model.CustomerRepository;
import com.structurizr.testapp.paperboy.service.PaperPriceService;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.function.Consumer;

import static com.structurizr.componentfinder.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalComponentFinderTest {


    private Container container;

    @Before
    public void setUp() {
        container = createDefaultContainer();
    }

    @Test
    public void findComponentsInMyApp() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithDirectDependencies(MATCH_EVERYTHING_REGEX);
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        new DirectDependenciesValidator(container).validateMyAppComponentDependencies(components);
    }

    @Test
    public void findComponentsInPaperboy() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithDirectDependencies(MATCH_EVERYTHING_REGEX);
        final Collection<Component> components = componentFinder.findComponents(PAPERBOY_APP_PACKAGE_TO_SCAN);
        new DirectDependenciesValidator(container).validatePaperBoyComponentDependencies(components);
    }

    @Test
    public void combineMultipleComponentsFinder() throws Exception {
        final FunctionalComponentFinder componentFinder = createComposedPaperBoyComponentsFinder();
        final Collection<Component> components = componentFinder.findComponents(PAPERBOY_APP_PACKAGE_TO_SCAN);
        new DirectDependenciesValidator(container).validatePaperBoyComponentDependencies(components);
        validatePaperBoyPackageTags();
    }

    @Test
    public void matchOnlyOneComponent() throws Exception {
        final FunctionalComponentFinder componentFinder = FunctionalComponentFinder.builder()
                .addComponentFactory(createComponentFactory(MATCH_CONTROLLER_SUFFIX_IN_MY_APP_REGEX))
                .withDirectDependencyScanner()
                .build();
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        assertThat(components).hasSize(1);
    }


    @Test
    public void findComponentsInPaperboyWithStructurizrDependencies() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithStructurizrDependencies(MATCH_EVERYTHING_REGEX);
        final Collection<Component> components = componentFinder.findComponents(PAPERBOY_APP_PACKAGE_TO_SCAN);
        new StructurizrDependenciesValidator(container).validatePaperBoyComponentDependencies(components);
    }

    @Test
    public void findComponentsInMyAppWithStructurizrDependencies() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithStructurizrDependencies(MATCH_EVERYTHING_REGEX);
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        new StructurizrDependenciesValidator(container).validateMyAppComponentDependencies(components);
    }

    private void validatePaperBoyPackageTags() {
        assertThat(getComponentOfType(CustomerFactory.class).getTags()).contains(FACTORY_TAG);
        assertThat(getComponentOfType(CustomerFactory.class).getTags()).doesNotContain(DOMAIN_TAG);

        assertThat(getComponentOfType(CustomerRepository.class).getTags()).doesNotContain(FACTORY_TAG);
        assertThat(getComponentOfType(CustomerRepository.class).getTags()).doesNotContain(DOMAIN_TAG);

        assertThat(getComponentOfType(Customer.class).getTags()).doesNotContain(FACTORY_TAG);
        assertThat(getComponentOfType(Customer.class).getTags()).contains(DOMAIN_TAG);

        assertThat(getComponentOfType(PaperPriceService.class).getTags()).contains(SERVICE_TAG);
    }

    private FunctionalComponentFinder createComposedPaperBoyComponentsFinder() {
        return FunctionalComponentFinder.builder()
                .addComponentFactory(createComponentFactoryWithTag(MATCH_ALL_UNDER_PAPERBOY_MODEL_PACKAGE_REGEX, ".*Factory", FACTORY_TAG))
                .addComponentFactory(createComponentFactoryWithTagForInverseOfRegex(MATCH_ALL_UNDER_PAPERBOY_MODEL_PACKAGE_REGEX, "(.*Factory)|(.*Repository)", DOMAIN_TAG))
                .addComponentFactory(createComponentFactoryWithTag(MATCH_ALL_UNDER_PAPERBOY_SERVICE_PACKAGE_REGEX, ".*Service", SERVICE_TAG))
                .addComponentFactory(createComponentFactoryWithTag(MATCH_PAPERBOY_PACKAGE_REGEX + "PapersService", ".*Service", SERVICE_TAG))
                .addComponentFactory(createComponentFactoryWithTag(MATCH_PAPERBOY_PACKAGE_REGEX + "ApplicationService", ".*Service", SERVICE_TAG))
                .withDirectDependencyScanner()
                .build();
    }

    private Component getComponentOfType(Class<?> aClass) {
        return container.getComponentOfType(aClass.getTypeName());
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

    private TypeBasedComponentFactory createComponentFactory(String typeRegex) {
        final Consumer<CreatedComponent> decorator = createBuilderWithStandardTags().build();
        return createComponentFactory(typeRegex, decorator);
    }

    private TypeBasedComponentFactory createComponentFactoryWithTag(String componentTypeRegex, String tagRegex, String additionalTag) {
        final Consumer<CreatedComponent> decorator = createBuilderWithStandardTags()
                .addTagForMatchingClass(tagRegex, additionalTag)
                .build();
        return createComponentFactory(componentTypeRegex, decorator);
    }

    private TypeBasedComponentFactory createComponentFactoryWithTagForInverseOfRegex(String componentTypeRegex, String tagRegex, String additionalTag) {
        final Consumer<CreatedComponent> decorator = createBuilderWithStandardTags()
                .addTagForNotMatchingClass(tagRegex, additionalTag)
                .build();
        return createComponentFactory(componentTypeRegex, decorator);
    }

    private TypeBasedComponentFactory createComponentFactory(String typeRegex, Consumer<CreatedComponent> decorator) {
        return TypeBasedComponentFactory.builder()
                .addTypeMatcher(typeRegex)
                .withBaseContainer(container)
                .withDecorator(decorator)
                .build();
    }


    private TagDecorator.Builder createBuilderWithStandardTags() {
        return TagDecorator.builder()
                .addTagForMatchingClass(MATCH_ALL_UNDER_PAPERBOY_PACKAGE_REGEX, PAPERBOY_TAG)
                .addTagForMatchingClass(MATCH_ALL_UNDER_MYAPP_PACKAGE_REGEX, MYAPP_TAG)
                .addTagForInterfaces(JAVA_INTERFACE)
                .addTagForClasses(JAVA_CLASS);
    }


}
