package com.structurizr.component;

import java.util.LinkedHashSet;
import java.util.Set;

public final class TypeRepository {

    private final Set<Type> types = new LinkedHashSet<>();

    public void add(Type type) {
        Type t = getType(type.getFullyQualifiedName());
        if (t == null) {
            // type isn't yet registered, so add it
            types.add(type);
        } else {
            if (type.getJavaClass() != null) {
                // this is the BCEL identified type
                t.setJavaClass(type.getJavaClass());
            } else {
                // this is the source code identified type
                t.setDescription(type.getDescription());
                t.setSource(type.getSource());
            }
        }
    }

    public Set<Type> getTypes() {
        return new LinkedHashSet<>(types);
    }

    Type getType(String fullyQualifiedClassName) {
        return types.stream().filter(t -> t.getFullyQualifiedName().equals(fullyQualifiedClassName)).findFirst().orElse(null);
    }

}