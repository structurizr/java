package com.structurizr.analysis;

import java.lang.reflect.Modifier;

public final class TypeVisibility {

    public static TypeVisibility valueOf(Class<?> type) {
        int modifiers = type.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            return PRIVATE;
        } else if (Modifier.isPublic(modifiers)) {
            return PUBLIC;
        } else if (Modifier.isProtected(modifiers)) {
            return PROTECTED;
        } else {
            return PACKAGE;
        }
    }

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
