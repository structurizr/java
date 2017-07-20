package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *   Represents a deployment node, which is something like:
 * </p>
 *
 * <ul>
 *     <li>Physical infrastructure (e.g. a physical server or device)</li>
 *     <li>Virtualised infrastructure (e.g. IaaS, PaaS, a virtual machine)</li>
 *     <li>Containerised infrastructure (e.g. a Docker container)</li>
 *     <li>Database server</li>
 *     <li>Java EE web/application server</li>
 *     <li>Microsoft IIS</li>
 *     <li>etc</li>
 * </ul>
 */
public final class DeploymentNode extends Element {

    private DeploymentNode parent;
    private Set<DeploymentNode> children = new HashSet<>();

    private String technology;
    private int instances = 1;

    private Set<ContainerInstance> containerInstances = new HashSet<>();

    @Override
    @JsonIgnore
    public Element getParent() {
        return parent;
    }

    void setParent(DeploymentNode parent) {
        this.parent = parent;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    @JsonIgnore
    protected Set<String> getRequiredTags() {
        return null;
    }

    @Override
    public String getTags() {
        return "";
    }

    @Override
    public String getCanonicalName() {
        if (getParent() != null) {
            return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
        } else {
            return CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
        }
    }

    public ContainerInstance add(Container container) {
        ContainerInstance containerInstance = getModel().addContainerInstance(container);
        this.containerInstances.add(containerInstance);

        return containerInstance;
    }

    public Set<ContainerInstance> getContainerInstances() {
        return new HashSet<>(containerInstances);
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology) {
        return addDeploymentNode(name, description, technology, 1);
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances) {
        return addDeploymentNode(name, description, technology, instances, null);
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances, Map<String, String> properties) {
        DeploymentNode deploymentNode = getModel().addDeploymentNode(this, name, description, technology, instances, properties);
        if (deploymentNode != null) {
            children.add(deploymentNode);
        }
        return deploymentNode;
    }

    public Set<DeploymentNode> getChildren() {
        return new HashSet<>(children);
    }

    void setChildren(Set<DeploymentNode> children) {
        this.children = children;
    }

    /**
     * @param name the name of the deployment node
     * @return the DeploymentNode instance with the specified name (or null if it doesn't exist).
     */
    public DeploymentNode getDeploymentNodeWithName(String name) {
        for (DeploymentNode deploymentNode : getChildren()) {
            if (deploymentNode.getName().equals(name)) {
                return deploymentNode;
            }
        }

        return null;
    }

    public Relationship uses(DeploymentNode destination, String description, String technology) {
        if (destination != null) {
            return getModel().addRelationship(this, destination, description, technology);
        } else {
            throw new IllegalArgumentException("");
        }
    }


}