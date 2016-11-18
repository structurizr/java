package com.structurizr.componentfinder;

import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;

import java.util.HashSet;
import java.util.Set;

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

    @Override
    public Set<String> getSupportingTypes(Component component) throws Exception {
        Set<String> referencedTypes = new HashSet<>();
        referencedTypes.addAll(componentFinderStrategy.getReferencedTypesInPackage(component.getType()));

        for (CodeElement codeElement : component.getCode()) {
            referencedTypes.addAll(componentFinderStrategy.getReferencedTypesInPackage(codeElement.getType()));
        }

        if (includeIndirectlyReferencedTypes) {
            int numberOfTypes = referencedTypes.size();
            boolean foundMore = true;
            while (foundMore) {
                Set<String> indirectlyReferencedTypes = new HashSet<>();
                for (String type : referencedTypes) {
                    indirectlyReferencedTypes.addAll(componentFinderStrategy.getReferencedTypesInPackage(type));
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
