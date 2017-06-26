package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ContainerInstance extends Element {

    private Container container;
    private String containerId;
    private int instanceId;

    ContainerInstance() {
    }

    ContainerInstance(Container container, int instanceId) {
        this.container = container;
        this.instanceId = instanceId;
    }

    @JsonIgnore
    public Container getContainer() {
        return container;
    }

    void setContainer(Container container) {
        this.container = container;
    }

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

    public int getInstanceId() {
        return instanceId;
    }

    void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    @JsonIgnore
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.CONTAINER_INSTANCE));
    }

    @Override
    public String getTags() {
        return container.getTags() + "," + super.getTags();
    }

    @Override
    @JsonIgnore
    public String getCanonicalName() {
        return container.getCanonicalName() + "[" + instanceId + "]";
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return container.getParent();
    }

    public Relationship uses(ContainerInstance destination, String description, String technology) {
        if (destination != null) {
            if (destination.getContainer().getParent().equals(this.getContainer().getParent())) {
                return getModel().addRelationship(this, destination, description, technology);
            } else {
                throw new IllegalArgumentException("");
            }
        } else {
            throw new IllegalArgumentException("");
        }
    }

}
