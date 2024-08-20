package com.structurizr.dsl;

import com.structurizr.model.GroupableElement;

abstract class GroupableElementDslContext extends ElementDslContext implements GroupableDslContext {

    private final ElementGroup group;

    GroupableElementDslContext() {
        this.group = null;
    }

    GroupableElementDslContext(ElementGroup group) {
        this.group = group;
    }

    @Override
    public boolean hasGroup() {
        return group != null;
    }

    @Override
    public ElementGroup getGroup() {
        return group;
    }

    abstract GroupableElement getElement();

}