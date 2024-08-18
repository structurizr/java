package com.structurizr.component;

import java.util.LinkedHashSet;
import java.util.Set;

final class TypeRepository {

    private final Set<Type> types = new LinkedHashSet<>();

    public void add(Type type) {
        this.types.add(type);
    }

    public Set<Type> getTypes() {
        return new LinkedHashSet<>(types);
    }

    Type getType(String fullyQualifiedClassName) {
        return types.stream().filter(t -> t.getFullyQualifiedName().equals(fullyQualifiedClassName)).findFirst().orElse(null);
    }

}