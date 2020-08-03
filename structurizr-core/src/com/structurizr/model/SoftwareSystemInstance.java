package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a deployment instance of a {@link SoftwareSystem}, which can be added to a {@link DeploymentNode}.
 */
public final class SoftwareSystemInstance extends StaticStructureElementInstance {

    private SoftwareSystem softwareSystem;
    private String softwareSystemId;

    SoftwareSystemInstance() {
    }

    SoftwareSystemInstance(SoftwareSystem softwareSystem, int instanceId, String environment) {
        super(instanceId, environment);

        setSoftwareSystem(softwareSystem);
        addTags(Tags.SOFTWARE_SYSTEM_INSTANCE);
    }

    @JsonIgnore
    public SoftwareSystem getSoftwareSystem() {
        return softwareSystem;
    }

    void setSoftwareSystem(SoftwareSystem softwareSystem) {
        this.softwareSystem = softwareSystem;
    }

    @Override
    public StaticStructureElement getElement() {
        return getSoftwareSystem();
    }

    /**
     * Gets the ID of the software system that this object represents a deployment instance of.
     *
     * @return  the software system ID, as a String
     */
    public String getSoftwareSystemId() {
        if (softwareSystem != null) {
            return softwareSystem.getId();
        } else {
            return softwareSystemId;
        }
    }

    void setSoftwareSystemId(String softwareSystemId) {
        this.softwareSystemId = softwareSystemId;
    }
    
}