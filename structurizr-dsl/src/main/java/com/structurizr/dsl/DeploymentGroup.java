package com.structurizr.dsl;

import com.structurizr.model.Element;

import java.util.Set;

class DeploymentGroup extends Element {

    private final Element parent;
    private final String name;

    DeploymentGroup(DeploymentEnvironment deploymentEnvironment, String name) {
        this.parent = deploymentEnvironment;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCanonicalName() {
        return name;
    }

    @Override
    public Element getParent() {
        return parent;
    }

    @Override
    public Set<String> getDefaultTags() {
        return null;
    }

}