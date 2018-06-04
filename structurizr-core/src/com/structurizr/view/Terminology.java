package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Provides a way for the terminology on diagrams, etc to be modified (e.g. language translations).
 */
public final class Terminology {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String enterprise = "";

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String person = "";

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String softwareSystem = "";

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String container = "";

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String component = "";

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String code = "";

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String deploymentNode = "";

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getSoftwareSystem() {
        return softwareSystem;
    }

    public void setSoftwareSystem(String softwareSystem) {
        this.softwareSystem = softwareSystem;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeploymentNode() {
        return deploymentNode;
    }

    public void setDeploymentNode(String deploymentNode) {
        this.deploymentNode = deploymentNode;
    }

}