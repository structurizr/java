package com.structurizr.component.supporting;

import com.structurizr.component.Type;
import com.structurizr.component.TypeRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A strategy that, given an interface, finds the implementation class with the specified prefix.
 */
public class ImplementationWithPrefixSupportingTypesStrategy implements SupportingTypesStrategy {

    private final String prefix;

    public ImplementationWithPrefixSupportingTypesStrategy(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Set<Type> findSupportingTypes(Type type, TypeRepository typeRepository) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("The type " + type.getFullyQualifiedName() + " is not an interface");
        }

        return typeRepository.getTypes().stream()
                    .filter(dependency -> dependency.implementsInterface(type) && dependency.getName().equals(prefix + type.getName()))
                    .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "ImplementationWithPrefixSupportingTypesStrategy{" +
                "prefix='" + prefix + '\'' +
                '}';
    }

}