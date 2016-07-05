package com.structurizr.componentfinder.func;

import com.structurizr.componentfinder.myapp.MyController;
import com.structurizr.componentfinder.myapp.MyRepository;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.testapp.paperboy.PaperDeliveryApplicationService;
import com.structurizr.testapp.paperboy.model.PaperBoy;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Consumer;

import static com.structurizr.componentfinder.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TypeBasedComponentFactoryTest {
    private Container container;

    @Before
    public void setUp() {
        container = createDefaultContainer();
    }

    @Test
    public void matchComponent() throws Exception {
        final TypeBasedComponentFactory componentFactory = createComponentFactory(MATCH_CONTROLLER_SUFFIX_IN_MY_APP_REGEX);
        final Component x = validateComponentIsCreatedForType(componentFactory, MyController.class);
        assertThat(x.getTags().contains(MYAPP_TAG)).isFalse();
    }

    @Test
    public void matchPackageComponent() throws Exception {
        final TypeBasedComponentFactory componentFactory = createComponentFactory(MATCH_ALL_UNDER_PAPERBOY_MODEL_PACKAGE_REGEX);
        validateComponentIsCreatedForType(componentFactory, PaperBoy.class);
    }


    @Test
    public void matchComponentWithTag() throws Exception {
        final TypeBasedComponentFactory componentFactory = createComponentFactoryWithTags(MATCH_CONTROLLER_SUFFIX_IN_MY_APP_REGEX);
        final Component x = validateComponentIsCreatedForType(componentFactory, MyController.class);
        assertThat(x.getTags().contains(MYAPP_TAG)).isTrue();
    }

    @Test
    public void notMatchComponent() throws Exception {
        final TypeBasedComponentFactory componentFactory = createComponentFactoryWithTags(MATCH_CONTROLLER_SUFFIX_IN_MY_APP_REGEX);
        validateComponentIsNotCreatedForType(componentFactory, MyRepository.class);
    }

    @Test
    public void matchMultipleComponents() throws Exception {
        final TypeBasedComponentFactory componentFactory = createComponentFactory(MATCH_CONTROLLER_SUFFIX_IN_MY_APP_REGEX, MATCH_ALL_UNDER_PAPERBOY_MODEL_PACKAGE_REGEX);
        validateComponentIsCreatedForType(componentFactory, MyController.class);
        validateComponentIsCreatedForType(componentFactory, PaperBoy.class);
        validateComponentIsNotCreatedForType(componentFactory, MyRepository.class);
        validateComponentIsNotCreatedForType(componentFactory, PaperDeliveryApplicationService.class);
    }

    @Test
    public void matchMultipleClassesBySuffix() throws Exception {
        final TypeBasedComponentFactory componentFactory = createComponentFactoryForSuffix(CONTROLLER_SUFFIX, REPOSITORY_SUFFIX);
        validateComponentIsCreatedForType(componentFactory, MyController.class);
        validateComponentIsCreatedForType(componentFactory, MyRepository.class);

        validateComponentIsNotCreatedForType(componentFactory, PaperBoy.class);
        validateComponentIsNotCreatedForType(componentFactory, PaperDeliveryApplicationService.class);
    }

    private Component validateComponentIsCreatedForType(TypeBasedComponentFactory componentFactory, Class<?> type) {
        final Optional<Component> component = componentFactory.createComponent(type);
        assertThat(component).isPresent();
        return component.get();
    }

    private void validateComponentIsNotCreatedForType(TypeBasedComponentFactory componentFactory, Class<?> type) {
        final Optional<Component> component = componentFactory.createComponent(type);
        assertThat(component.isPresent()).isFalse();
    }

    private TypeBasedComponentFactory createComponentFactoryWithTags(String typeRegex) {
        final Consumer<CreatedComponent> decorator = createStandardTags().build();
        return createComponentFactoryWithTags(typeRegex, decorator);
    }


    private TypeBasedComponentFactory createComponentFactoryWithTags(String typeRegex, Consumer<CreatedComponent> decorator) {
        return TypeBasedComponentFactory.builder()
                .addTypeMatcher(typeRegex)
                .withBaseContainer(container)
                .withDecorator(decorator)
                .build();
    }

    private TypeBasedComponentFactory createComponentFactory(String... typeRegex) {
        final TypeBasedComponentFactory.Builder builder = TypeBasedComponentFactory.builder()
                .withBaseContainer(container);
        for (String matchType : typeRegex) {
            builder.addTypeMatcher(matchType);
        }
        return builder.build();
    }

    private TypeBasedComponentFactory createComponentFactoryForSuffix(String... classSuffixes) {
        final TypeBasedComponentFactory.Builder builder = TypeBasedComponentFactory.builder()
                .withBaseContainer(container);
        for (String suffix : classSuffixes) {
            builder.addSuffixTypeMatcher(suffix);
        }
        return builder.build();
    }


    private TagDecorator.Builder createStandardTags() {
        return TagDecorator.builder()
                .addTagForMatchingClass(MATCH_ALL_UNDER_PAPERBOY_PACKAGE_REGEX, PAPERBOY_TAG)
                .addTagForMatchingClass(MATCH_ALL_UNDER_MYAPP_PACKAGE_REGEX, MYAPP_TAG);
    }
}