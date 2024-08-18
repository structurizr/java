package com.structurizr.component.visitor;

import com.structurizr.model.Component;

/**
 * Provides a way to visit each discovered component.
 */
public interface ComponentVisitor {

    void visit(Component component);

}