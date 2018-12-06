package com.structurizr.model;

/**
 * This is the superclass for model elements that describe deployment nodes and container instances.
 */
public abstract class DeploymentElement extends Element {

    static final String DEFAULT_DEPLOYMENT_ENVIRONMENT = "Default";

    private String environment = DEFAULT_DEPLOYMENT_ENVIRONMENT;

    DeploymentElement() {
    }

    public String getEnvironment() {
        return environment;
    }

    void setEnvironment(String environment) {
        this.environment = environment;
    }

}