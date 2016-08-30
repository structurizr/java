package com.structurizr.componentfinder.func;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

enum PackageNameValidator implements Predicate<String>, Consumer<String> {
    INSTANCE;
    public static final String MATCH_ANY_PACKAGE_REGEX = "(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    private static Pattern p = Pattern.compile(MATCH_ANY_PACKAGE_REGEX);


    @Override
    public boolean test(String packageName) {
        return p.matcher(packageName).matches();
    }


    @Override
    public void accept(String packageName) {
        if (!test(packageName))
            throw new RuntimeException("Invalid package name " + packageName + ". Make sure you enter a valid package name and not a regex");
    }


}

