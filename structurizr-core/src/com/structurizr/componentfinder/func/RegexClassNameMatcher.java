package com.structurizr.componentfinder.func;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RegexClassNameMatcher implements Predicate<Class<?>> {
    private final Pattern pattern;

    private RegexClassNameMatcher(String packageName) {
        this.pattern = Pattern.compile(packageName);
    }

    public static Predicate<Class<?>> create(String packageName) {
        return new RegexClassNameMatcher(packageName);
    }


    @Override
    public boolean test(Class type) {
        final String name = type.getName();
        return pattern.matcher(name).matches();
    }
}
