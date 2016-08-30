package com.structurizr.componentfinder.func;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.structurizr.model.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TagDecorator implements Consumer<CreatedComponent> {
    private static final Predicate<Class<?>> IS_INTERFACE = Class::isInterface;
    private final Function<Class<?>, Collection<String>> tagFunction;

    private TagDecorator(Function<Class<?>, Collection<String>> tagFunction) {
        this.tagFunction = tagFunction;
    }


    public static Consumer<CreatedComponent> create(Function<Class<?>, Collection<String>> tagFunction) {
        return new TagDecorator(tagFunction);
    }

    public static Builder builder() {
        return new Builder();
    }


    @Override
    public void accept(CreatedComponent createdComponent) {
        final Component component = createdComponent.getComponent();
        final Collection<String> tag = tagFunction.apply(createdComponent.getOriginClass());
        component.addTags(tag.toArray(new String[tag.size()]));
    }


    public static final class Builder {
        private ImmutableList.Builder<Function<Class<?>, Collection<String>>> builder = ImmutableList.builder();

        private Builder() {
        }


        public Consumer<CreatedComponent> build() {
            return TagDecorator.create(buildFunction());
        }

        public Builder addTag(Predicate<Class<?>> tagCondition, String tag) {
            builder.add(createTagFunction(tagCondition, tag));
            return this;
        }

        public Builder addTagForInterfaces( String tag) {
            builder.add(createTagFunction(IS_INTERFACE, tag));
            return this;
        }

        public Builder addTagForClasses(String tag) {
            builder.add(createTagFunction(IS_INTERFACE.negate(), tag));
            return this;
        }

        public Builder addTagForMatchingClass(String regex, String tag) {
            return addTag(RegexClassNamePredicate.create(regex), tag);
        }

        public Builder addTagForNotMatchingClass(String regex, String tag) {
            return addTag(RegexClassNamePredicate.create(regex).negate(), tag);
        }


        private Function<Class<?>, Collection<String>> createTagFunction(Predicate<Class<?>> tagCondition, String tag) {
            return (Class<?> x) -> {
                if (tagCondition.test(x))
                    return Lists.newArrayList(tag);
                else
                    return Collections.emptyList();
            };
        }

        private Function<Class<?>, Collection<String>> buildFunction() {
            final ImmutableList<Function<Class<?>, Collection<String>>> list = builder.build();
            return (Class<?> x) -> {
                final ImmutableList.
                        Builder<String> allTags = ImmutableList.builder();
                for (Function<Class<?>, Collection<String>> f : list) {
                    final Collection<String> tags = f.apply(x);
                    allTags.addAll(tags);
                }
                return allTags.build();
            };
        }
    }
}
