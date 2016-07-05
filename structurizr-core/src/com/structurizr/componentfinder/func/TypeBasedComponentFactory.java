package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

public class TypeBasedComponentFactory {
    private final Predicate<Class<?>> typeMatcher;
    private final Function<Class<?>, CreatedComponent> factory;
    private final Consumer<CreatedComponent> decorator;


    private TypeBasedComponentFactory(Builder builder) {
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
        private Predicate<Class<?>> typeMatcher = (Class<?> x) -> false;
        private Function<Class<?>, CreatedComponent> factory;
        private Consumer<CreatedComponent> decorator = (x) -> {
        };

        private Builder() {
        }

        public Builder withTypeMatcher(Predicate<Class<?>> val) {
            checkNotNull(val);
            this.typeMatcher = typeMatcher.or(val);
            return this;
        }

        public Builder addTypeMatcher(String typeRegex) {
            return withTypeMatcher(createNonInnerClassRegexTypeMatcher(typeRegex));

        }

        public Builder withFactory(Function<Class<?>, CreatedComponent> val) {
            checkNotNull(val);
            factory = val;
            return this;
        }

        public Builder withDecorator(Consumer<CreatedComponent> val) {
            checkNotNull(val);
            decorator = val;
            return this;
        }

        public Builder withBaseContainer(Container c) {
            factory = (Class<?> x) -> CreatedComponent.createFromClass(c, x);
            return this;
        }

        public Builder addSuffixTypeMatcher(String suffix) {
            return addTypeMatcher(".*" + suffix);
        }

        public TypeBasedComponentFactory build() {
            checkNotNull(factory, "A component factory is required.");
            return new TypeBasedComponentFactory(this);
        }

        private Predicate<Class<?>> createNonInnerClassRegexTypeMatcher(String typeRegex) {
            return RegexClassNameMatcher.create(typeRegex)
                    .and(InnerClassMatcher.INSTANCE.negate());
        }


    }
}
