package com.structurizr.dsl;

import com.structurizr.PropertyHolder;

import java.util.ArrayList;
import java.util.Collection;

final class PropertiesDslContext extends DslContext {

    private final Collection<PropertyHolder> propertyHolders = new ArrayList<>();

    public PropertiesDslContext(PropertyHolder propertyHolder) {
        this.propertyHolders.add(propertyHolder);
    }

    public PropertiesDslContext(Collection<PropertyHolder> propertyHolders) {
        this.propertyHolders.addAll(propertyHolders);
    }

    Collection<PropertyHolder> getPropertyHolders() {
        return this.propertyHolders;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}