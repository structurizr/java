package com.structurizr.componentfinder.func;

import com.structurizr.componentfinder.myapp.MyController;
import com.structurizr.componentfinder.myapp.MyRepository;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Consumer;

import static com.structurizr.componentfinder.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ComponentFactoryTest {
    private Container container;

    @Before
    public void setUp() {
        container = createDefaultContainer();
    }

    @Test
    public void matchComponent() throws Exception {
        final ComponentFactory componentFactory = createComponentFactory(".*\\.myapp\\.*Controller");
        final Optional<Component> component = componentFactory.createComponent(MyController.class);
        assertThat(component.isPresent()).isTrue();
        component.ifPresent(
                x -> assertThat(x.hasTag(MYAPP_TAG)).isFalse()
        );
    }

    @Test
    public void matchComponentWithTag() throws Exception {
        final ComponentFactory componentFactory = createComponentFactoryWithTags(".*\\.myapp\\.*Controller");
        final Optional<Component> component = componentFactory.createComponent(MyController.class);
        assertThat(component.isPresent()).isTrue();
        component.ifPresent(
                x -> assertThat(x.hasTag(MYAPP_TAG)).isTrue()
        );
    }

    @Test
    public void notMatchComponent() throws Exception {
        final ComponentFactory componentFactory = createComponentFactoryWithTags(".*\\.myapp\\.*Controller");
        final Optional<Component> component = componentFactory.createComponent(MyRepository.class);
        assertThat(component.isPresent()).isFalse();
    }

    private ComponentFactory createComponentFactoryWithTags(String typeRegex) {
        final Consumer<CreatedComponent> decorator = createStandardTags().build();
        return createComponentFactoryWithTags(typeRegex, decorator);
    }


    private ComponentFactory createComponentFactoryWithTags(String typeRegex, Consumer<CreatedComponent> decorator) {
        return ComponentFactory.builder()
                .withTypeMatching(typeRegex)
                .withFactoryFromTypeForContainer(container)
                .withDecorator(decorator)
                .build();
    }

    private ComponentFactory createComponentFactory(String typeRegex) {
        return ComponentFactory.builder()
                .withTypeMatching(typeRegex)
                .withFactoryFromTypeForContainer(container)
                .build();
    }


    private TagDecorator.Builder createStandardTags() {
        return TagDecorator.builder()
                .addTagForMatchingClass(MATCH_PAPERBOY_PACKAGE_REGEX, PAPERBOY_TAG)
                .addTagForMatchingClass(MATCH_MYAPP_PACKAGE_REGEX, MYAPP_TAG);
    }
}