package com.structurizr.analysis;

public final class TypeVisibility {

    public static final TypeVisibility PUBLIC = new TypeVisibility("public");
    public static final TypeVisibility PACKAGE = new TypeVisibility("package");
    public static final TypeVisibility PROTECTED = new TypeVisibility("protected");
    public static final TypeVisibility PRIVATE = new TypeVisibility("private");

    private String name;

    private TypeVisibility(String name) {
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
