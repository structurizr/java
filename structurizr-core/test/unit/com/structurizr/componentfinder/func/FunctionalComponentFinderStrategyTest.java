package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.function.Predicate;

import static com.structurizr.componentfinder.ComponentFinderTestConstants.*;

public class FunctionalComponentFinderStrategyTest {

    private static final Predicate<Class<?>> IS_INTERFACE = Class::isInterface;

    private Container webApplication;

    @Before
    public void setUp() {
        webApplication = createDefaultContainer();
    }

    @Test
    public void findComponentsInMyApp() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithDirectDependencies(".*");
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        new DirectDependenciesValidator(webApplication).validateMyAppComponentDependencies(components);
    }

    @Test
    public void findComponentsInPaperboy() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithDirectDependencies(".*");//TODO scan for sub packages
        final Collection<Component> components = componentFinder.findComponents(PAPERBOY_APP_PACKAGE_TO_SCAN);
        new DirectDependenciesValidator(webApplication).validatePaperBoyComponentDependencies(components);
    }
    @Test
    public void findComponentsInMyAppWithOriginalDependencies() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithStructurizrDependencies(".*");
        final Collection<Component> components = componentFinder.findComponents(MY_APP_TEST_PACKAGE_TO_SCAN);
        new StructurizrDependenciesValidator(webApplication).validateMyAppComponentDependencies(components);
    }

    @Test
    public void findComponentsInPaperboyWithOriginalDependencies() throws Exception {
        final FunctionalComponentFinder componentFinder = createComponentFinderWithStructurizrDependencies(".*");//TODO scan for sub packages
        final Collection<Component> components = componentFinder.findComponents(PAPERBOY_APP_PACKAGE_TO_SCAN);
        new StructurizrDependenciesValidator(webApplication).validatePaperBoyComponentDependencies(components);
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
        return ComponentFactory.builder()
                .withTypeMatcher(
                        RegexClassNameMatcher.create(typeRegex)
                                .and(InnerClassMatcher.INSTANCE).negate())
                .withFactoryFromTypeForContainer(webApplication)
                .withDecorator(
                        TagDecorator.builder()
                                .addTagForMatchingClass(".*\\.paperboy\\..*", PAPERBOY_TAG)
                                .addTagForMatchingClass(".*\\.myapp\\..*", MYAPP_TAG)
                                .addTag(IS_INTERFACE, JAVA_INTERFACE)
                                .addTag(IS_INTERFACE.negate(), JAVA_CLASS)
                                .build())
                .build();
    }


}
