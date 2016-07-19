package com.structurizr.componentfinder.func;

import java.util.function.Predicate;
import java.util.regex.Pattern;

class RegexClassNamePredicate implements Predicate<Class<?>> {
    private final Pattern pattern;

    private RegexClassNamePredicate(String packageName) {
        this.pattern = Pattern.compile(packageName);
    }

    public static Predicate<Class<?>> createNonInnerClassRegexPredicate(String typeRegex) {
        return RegexClassNamePredicate.create(typeRegex)
                .and(InnerClassPredicate.INSTANCE.negate());
    }

    public static Predicate<Class<?>> create(String className) {
        return new RegexClassNamePredicate(className);
    }

    public static Predicate<Class<?>> createSuffixPredicate(String suffix) {
        return createNonInnerClassRegexPredicate(".*" + suffix);
    }


    @Override
    public boolean test(Class type) {
        final String name = type.getName();
        return pattern.matcher(name).matches();
    }
}
