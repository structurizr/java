package com.structurizr.dsl.example;

import com.structurizr.component.Type;
import com.structurizr.component.matcher.TypeMatcher;

public class CustomTypeMatcher implements TypeMatcher {

    @Override
    public boolean matches(Type type) {
        return false;
    }

    @Override
    public String toString() {
        return "CustomTypeMatcher{}";
    }

}