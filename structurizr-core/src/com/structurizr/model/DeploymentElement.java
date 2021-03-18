package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is the superclass for model elements that describe deployment nodes, infrastructure nodes, and container instances.
 */
public abstract class DeploymentElement extends Element {

    public static final String DEFAULT_DEPLOYMENT_ENVIRONMENT = "Default";
    public static final String DEFAULT_DEPLOYMENT_GROUP = "Default";

    private DeploymentNode parent;
    private String environment = DEFAULT_DEPLOYMENT_ENVIRONMENT;

    DeploymentElement() {
    }

    /**
     * Gets the parent deployment node.
     *
     * @return  the parent DeploymentNode, or null if there is no parent
     */
    @Override
    @JsonIgnore
    public final Element getParent() {
        return parent;
    }

    void setParent(DeploymentNode parent) {
        this.parent = parent;
    }

    public String getEnvironment() {
        return environment;
    }

    void setEnvironment(String environment) {
        this.environment = environment;
    }

}