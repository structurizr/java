package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a deployment instance of a {@link Container}, which can be added to a {@link DeploymentNode}.
 */
public final class ContainerInstance extends StaticStructureElementInstance {

    private Container container;
    private String containerId;

    ContainerInstance() {
    }

    ContainerInstance(Container container, int instanceId, String environment, String deploymentGroup) {
        super(instanceId, environment, deploymentGroup);

        setContainer(container);
        addTags(Tags.CONTAINER_INSTANCE);
    }

    @JsonIgnore
    public Container getContainer() {
        return container;
    }

    void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public StaticStructureElement getElement() {
        return getContainer();
    }

    /**
     * Gets the ID of the container that this object represents a deployment instance of.
     *
     * @return  the container ID, as a String
     */
    public String getContainerId() {
        if (container != null) {
            return container.getId();
        } else {
            return containerId;
        }
    }

    void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @Override
    @JsonIgnore
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

}