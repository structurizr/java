package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

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

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String infrastructureNode = "";

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String relationship = "";

    public String getEnterprise() {
        return enterprise;
    }

    @Deprecated
    void setEnterprise(String enterprise) {
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

    public String getInfrastructureNode() {
        return infrastructureNode;
    }

    public void setInfrastructureNode(String infrastructureNode) {
        this.infrastructureNode = infrastructureNode;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    /**
     * Finds the terminology that can be used to describe/label the specified model item.
     *
     * @param modelItem     an Element or Relationship
     * @return  the default or overridden terminology for the specified model item
     */
    public String findTerminology(ModelItem modelItem) {
        if (modelItem instanceof StaticStructureElementInstance) {
            modelItem = ((StaticStructureElementInstance)modelItem).getElement();
        }

        if (modelItem instanceof Relationship) {
            return !StringUtils.isNullOrEmpty(getRelationship()) ? getRelationship() : "Relationship";
        } else if (modelItem instanceof Person) {
            return !StringUtils.isNullOrEmpty(getPerson()) ? getPerson() : "Person";
        } else if (modelItem instanceof SoftwareSystem) {
            return !StringUtils.isNullOrEmpty(getSoftwareSystem()) ? getSoftwareSystem() : "Software System";
        } else if (modelItem instanceof Container) {
            return !StringUtils.isNullOrEmpty(getContainer()) ? getContainer() : "Container";
        } else if (modelItem instanceof Component) {
            return !StringUtils.isNullOrEmpty(getComponent()) ? getComponent() : "Component";
        } else if (modelItem instanceof DeploymentNode) {
            return !StringUtils.isNullOrEmpty(getDeploymentNode()) ? getDeploymentNode() : "Deployment Node";
        } else if (modelItem instanceof InfrastructureNode) {
            return !StringUtils.isNullOrEmpty(getInfrastructureNode()) ? getInfrastructureNode() : "Infrastructure Node";
        }

        throw new IllegalArgumentException("Unknown model item type.");
    }

}