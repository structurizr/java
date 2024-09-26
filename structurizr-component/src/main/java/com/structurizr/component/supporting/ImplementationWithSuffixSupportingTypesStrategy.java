package com.structurizr.component.supporting;

import com.structurizr.component.Type;
import com.structurizr.component.TypeRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A strategy that, given an interface, finds the implementation class with the specified suffix.
 */
public class ImplementationWithSuffixSupportingTypesStrategy implements SupportingTypesStrategy {

    private final String suffix;

    public ImplementationWithSuffixSupportingTypesStrategy(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Set<Type> findSupportingTypes(Type type, TypeRepository typeRepository) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("The type " + type.getFullyQualifiedName() + " is not an interface");
        }

        return typeRepository.getTypes().stream()
                    .filter(dependency -> dependency.implementsInterface(type) && dependency.getName().equals(type.getName() + suffix))
                    .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "ImplementationWithSuffixSupportingTypesStrategy{" +
                "suffix='" + suffix + '\'' +
                '}';
    }

}