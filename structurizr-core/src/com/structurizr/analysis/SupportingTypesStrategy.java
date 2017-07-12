package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * Superclass for strategies used to find the types that support a component.
 */
public abstract class SupportingTypesStrategy {

    private TypeRepository typeRepository;

    protected TypeRepository getTypeRepository() {
        return typeRepository;
    }

    void setTypeRepository(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public abstract Set<String> findSupportingTypes(Component component) throws Exception;

}
