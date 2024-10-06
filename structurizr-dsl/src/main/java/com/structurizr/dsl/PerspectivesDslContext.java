package com.structurizr.dsl;

import com.structurizr.model.ModelItem;

import java.util.ArrayList;
import java.util.Collection;

final class PerspectivesDslContext extends DslContext {

    private final Collection<ModelItem> modelItems = new ArrayList<>();

    PerspectivesDslContext(ModelItem modelItem) {
        this.modelItems.add(modelItem);
    }

    PerspectivesDslContext(Collection<ModelItem> modelItems) {
        this.modelItems.addAll(modelItems);
    }

    Collection<ModelItem> getModelItems() {
        return this.modelItems;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}