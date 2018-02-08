package com.structurizr.analysis;

import java.lang.reflect.Modifier;

public final class TypeCategory {

    public static TypeCategory valueOf(Class<?> type) {
        if (type.isInterface()) {
            return INTERFACE;
        } else if (type.isEnum()) {
            return ENUM;
        } else if (Modifier.isAbstract(type.getModifiers())) {
            return ABSTRACT_CLASS;
        } else{
            return CLASS;
        }
    }

    public static final TypeCategory CLASS = new TypeCategory("class");
    public static final TypeCategory INTERFACE = new TypeCategory("interface");
    public static final TypeCategory ABSTRACT_CLASS = new TypeCategory("abstract");
    public static final TypeCategory ENUM = new TypeCategory("enum");

    private String name;

    private TypeCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
