package com.structurizr.analysis;

import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * This strategy finds all types that are referenced by the component type
 * and supporting types.
 */
public class ReferencedTypesSupportingTypesStrategy extends SupportingTypesStrategy {

    private boolean includeIndirectlyReferencedTypes;

    public ReferencedTypesSupportingTypesStrategy() {
        this(true);
    }

    public ReferencedTypesSupportingTypesStrategy(boolean includeIndirectlyReferencedTypes) {
        this.includeIndirectlyReferencedTypes = includeIndirectlyReferencedTypes;
    }

    private Set<Class<?>> getReferencedTypesInPackage(String type) {
        return getTypeRepository()
                .findReferencedTypes(type)
                .stream()
                .filter(this::accepts)
                .collect(toSet());
    }

    private boolean accepts(final Class<?> clazz) {
        final String type = clazz.getCanonicalName();

        if (type != null) {
            for (final String packageName : getTypeRepository().getPackages()) {
                if (type.startsWith(packageName)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Set<Class<?>> findSupportingTypes(Component component) {
        Set<Class<?>> referencedTypes = new HashSet<>();
        referencedTypes.addAll(getReferencedTypesInPackage(component.getType().getType()));

        for (CodeElement codeElement : component.getCode()) {
            referencedTypes.addAll(getReferencedTypesInPackage(codeElement.getType()));
        }

        if (includeIndirectlyReferencedTypes) {
            int numberOfTypes = referencedTypes.size();
            boolean foundMore = true;
            while (foundMore) {
                Set<Class<?>> indirectlyReferencedTypes = new HashSet<>();
                for (Class<?> type : referencedTypes) {
                    indirectlyReferencedTypes.addAll(getReferencedTypesInPackage(type.getCanonicalName()));
                }
                referencedTypes.addAll(indirectlyReferencedTypes);

                if (referencedTypes.size() > numberOfTypes) {
                    foundMore = true;
                    numberOfTypes = referencedTypes.size();
                } else {
                    foundMore = false;
                }
            }
        }

        return referencedTypes;
    }

}
