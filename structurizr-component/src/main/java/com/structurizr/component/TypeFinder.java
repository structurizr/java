package com.structurizr.component;

import com.structurizr.component.filter.TypeFilter;
import com.structurizr.component.provider.TypeProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Set;

class TypeFinder {

    private static final Log log = LogFactory.getLog(TypeFinder.class);

    void run(Collection<TypeProvider> typeProviders, TypeFilter typeFilter, TypeRepository typeRepository) {
        for (TypeProvider typeProvider : typeProviders) {
            log.debug("Running " + typeProvider.toString());

            Set<Type> types = typeProvider.getTypes();
            for (com.structurizr.component.Type type : types) {

                boolean accepted = typeFilter.accept(type);
                if (accepted) {
                    log.debug(" + " + type.getFullyQualifiedName() + " (accepted=true)");
                } else {
                    log.debug(" - " + type.getFullyQualifiedName() + " (accepted=false)");
                }

                if (accepted) {
                    typeRepository.add(type);
                }
            }
        }
    }

}