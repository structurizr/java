package com.structurizr.analysis;

public final class TypeCategory {

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
