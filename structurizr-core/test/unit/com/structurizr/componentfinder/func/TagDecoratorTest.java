package com.structurizr.componentfinder.func;

import com.google.common.collect.ImmutableList;
import com.structurizr.componentfinder.myapp.MyController;
import com.structurizr.componentfinder.myapp.MyRepository;
import com.structurizr.componentfinder.myapp.MyRepositoryImpl;
import com.structurizr.model.Component;
import org.junit.Test;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.structurizr.componentfinder.TestConstants.createDefaultContainer;
import static com.structurizr.componentfinder.func.TagValidator.validateTags;

public class TagDecoratorTest {

    private static final String CONTROLLER_TAG = "Controller";
    private static final String REPOSITORY_TAG = "Repository";
    private static final String INTERFACE_TAG = "INTERFACE";
    private static final String CLASS_TAG = "CLASS";
    private static final Predicate<Class<?>> IS_INTERFACE_PREDICATE = Class::isInterface;
    private static final Predicate<Class<?>> IS_CLASS_PREDICATE = IS_INTERFACE_PREDICATE.negate();

    @Test
    public void tagMyController() throws Exception {
        validateTagComponentForClass(MyController.class, CONTROLLER_TAG, CLASS_TAG);

    }

    @Test
    public void tagMyRepository() throws Exception {
        validateTagComponentForClass(MyRepository.class, REPOSITORY_TAG, INTERFACE_TAG);
    }

    @Test
    public void tagRepositoryImpl() throws Exception {
        validateTagComponentForClass(MyRepositoryImpl.class, REPOSITORY_TAG, CLASS_TAG);
    }


    private void validateTagComponentForClass(Class<?> baseClassForComponent, String... expectedTags) throws Exception {
        validateTagComponentForClass(baseClassForComponent, createDecoratorThroughFactoryMethod(), expectedTags);
        validateTagComponentForClass(baseClassForComponent, createDecoratorThroughBuilder(), expectedTags);
    }

    private Consumer<CreatedComponent> createDecoratorThroughBuilder() {
        return TagDecorator.builder()
                .addTagForMatchingClass(".*\\.MyController", CONTROLLER_TAG)
                .addTagForMatchingClass(".*\\.MyRepository.*", REPOSITORY_TAG)
                .addTag(IS_INTERFACE_PREDICATE, INTERFACE_TAG)
                .addTag(IS_CLASS_PREDICATE, CLASS_TAG)
                .build();
    }

    private void validateTagComponentForClass(Class<?> componentSourceClass, Consumer<CreatedComponent> tagDecorator, String[] expectedTags) {
        final Component component = createDecoratedComponent(tagDecorator, componentSourceClass);
        validateTags(component, expectedTags);
    }

    private Consumer<CreatedComponent> createDecoratorThroughFactoryMethod() {
        return TagDecorator.create(this::generateAllTags);
    }

    private Component createDecoratedComponent(Consumer<CreatedComponent> decorator, Class<?> originClass) {
        final CreatedComponent createdComponent = CreatedComponent.createFromClass(createDefaultContainer(), originClass);
        decorator.accept(createdComponent);
        return createdComponent.getComponent();
    }

    private Collection<String> generateAllTags(Class<?> aClass) {
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        if (aClass.getName().contains("MyController"))
            builder.add(CONTROLLER_TAG);
        if (aClass.getName().contains("MyRepository"))
            builder.add(REPOSITORY_TAG);
        if (aClass.isInterface())
            builder.add(INTERFACE_TAG);
        else
            builder.add(CLASS_TAG);
        return builder.build();
    }

}