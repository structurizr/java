package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.ModelItem;

import java.util.Set;
import java.util.stream.Collectors;

class ElementsDslContext extends ModelItemsDslContext {

    private final Set<Element> elements;

    ElementsDslContext(DslContext parentDslContext, Set<Element> elements) {
        super(parentDslContext);

        this.elements = elements;
    }

    Set<Element> getElements() {
        return elements;
    }

    @Override
    Set<ModelItem> getModelItems() {
        return elements.stream().map(e -> (ModelItem)e).collect(Collectors.toSet());
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}