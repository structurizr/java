package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

public class ComponentFactory {
    private final Predicate<Class<?>> typeMatcher;
    private final Function<Class<?>, CreatedComponent> factory;
    private final Consumer<CreatedComponent> decorator;


    private ComponentFactory(Builder builder) {
        typeMatcher = builder.typeMatcher;
        factory = builder.factory;
        decorator = builder.decorator;
    }

    public static Builder builder() {
        return new Builder();
    }


    public Optional<Component> createComponent(Class<?> type) {
        if (typeMatcher.test(type))
            return create(type);
        else
            return Optional.empty();

    }

    private Optional<Component> create(Class<?> type) {
        final CreatedComponent createdComponent = factory.apply(type);
        decorator.accept(createdComponent);
        return Optional.of(createdComponent.getComponent());
    }


    public static final class Builder {
        private Predicate<Class<?>> typeMatcher;
        private Function<Class<?>, CreatedComponent> factory;
        private Consumer<CreatedComponent> decorator;

        private Builder() {
        }

        public Builder withTypeMatcher(Predicate<Class<?>> val) {
            typeMatcher = val;
            return this;
        }

        public Builder withTypeMatching(String typeRegex) {
            return withTypeMatcher(createNonInnerClassRegexTypeMatcher(typeRegex));

        }

        public Builder withFactory(Function<Class<?>, CreatedComponent> val) {
            factory = val;
            return this;
        }

        public Builder withDecorator(Consumer<CreatedComponent> val) {
            decorator = val;
            return this;
        }

        public Builder withFactoryFromTypeForContainer(Container c) {
            factory = (Class<?> x) -> CreatedComponent.createFromClass(c, x);
            return this;
        }

        public ComponentFactory build() {
            checkNotNull(typeMatcher, "A type matcher is required.");
            checkNotNull(factory, "A component factory is required.");
            checkNotNull(decorator, "A decorator is required.");
            return new ComponentFactory(this);
        }

        private Predicate<Class<?>> createNonInnerClassRegexTypeMatcher(String typeRegex) {
            return RegexClassNameMatcher.create(typeRegex)
                    .and(InnerClassMatcher.INSTANCE).negate();
        }


    }
}
