package com.structurizr.component;

import java.util.HashSet;
import java.util.Set;

final class TypeRepository {

    private final Set<Type> types = new HashSet<>();

    public void add(Type type) {
        this.types.add(type);
    }

    public Set<Type> getTypes() {
        return new HashSet<>(types);
    }

    Type getType(String fullyQualifiedClassName) {
        return types.stream().filter(t -> t.getFullyQualifiedName().equals(fullyQualifiedClassName)).findFirst().orElse(null);
    }

}