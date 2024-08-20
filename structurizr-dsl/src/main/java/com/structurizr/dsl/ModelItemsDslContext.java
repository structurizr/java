package com.structurizr.dsl;

import com.structurizr.model.ModelItem;

import java.util.Set;

abstract class ModelItemsDslContext extends DslContext {

    private final DslContext parentDslContext;

    public ModelItemsDslContext() {
        this.parentDslContext = null;
    }

    ModelItemsDslContext(DslContext parentDslContext) {
        this.parentDslContext = parentDslContext;
    }

    DslContext getParentDslContext() {
        return parentDslContext;
    }

    abstract Set<ModelItem> getModelItems();

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}