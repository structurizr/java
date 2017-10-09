package com.structurizr.analysis;

import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    private Set<Class<?>> getReferencedTypesInPackage(String type) throws Exception {
        return getTypeRepository().findReferencedTypes(type).stream().filter(t -> t.getCanonicalName() != null && t.getCanonicalName().startsWith(getTypeRepository().getPackage())).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findSupportingTypes(Component component) throws Exception {
        Set<Class<?>> referencedTypes = new HashSet<>();
        referencedTypes.addAll(getReferencedTypesInPackage(component.getType()));

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

        return referencedTypes.stream().map(Class::getName).collect(Collectors.toSet());
    }

}
