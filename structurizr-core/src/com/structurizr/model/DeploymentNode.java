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
public final class DeploymentNode extends DeploymentElement {

    private DeploymentNode parent;
    private String technology;
    private int instances = 1;

    private Set<DeploymentNode> children = new HashSet<>();
    private Set<ContainerInstance> containerInstances = new HashSet<>();

    /**
     * Adds a container instance to this deployment node, replicating all of the container-container relationships.
     *
     * @param container     the Container to add an instance of
     * @return  a ContainerInstance object
     */
    public ContainerInstance add(Container container) {
        return add(container, true);
    }

    /**
     * Adds a container instance to this deployment node, optionally replicating all of the container-container relationships.
     *
     * @param container                         the Container to add an instance of
     * @param replicateContainerRelationships   true if the container-container relationships should be replicated between the container instances, false otherwise
     * @return  a ContainerInstance object
     */
    public ContainerInstance add(Container container, boolean replicateContainerRelationships) {
        ContainerInstance containerInstance = getModel().addContainerInstance(this, container, replicateContainerRelationships);
        this.containerInstances.add(containerInstance);

        return containerInstance;
    }

    /**
     * Adds a child deployment node.
     *
     * @param name          the name of the deployment node
     * @param description   a short description
     * @param technology    the technology
     * @return  a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology) {
        return addDeploymentNode(name, description, technology, 1);
    }

    /**
     * Adds a child deployment node.
     *
     * @param name          the name of the deployment node
     * @param description   a short description
     * @param technology    the technology
     * @param instances     the number of instances
     * @return  a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances) {
        return addDeploymentNode(name, description, technology, instances, null);
    }

    /**
     * Adds a child deployment node.
     *
     * @param name          the name of the deployment node
     * @param description   a short description
     * @param technology    the technology
     * @param instances     the number of instances
     * @param properties    a Map (String,String) describing name=value properties
     * @return  a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances, Map<String, String> properties) {
        DeploymentNode deploymentNode = getModel().addDeploymentNode(this, this.getEnvironment(), name, description, technology, instances, properties);
        if (deploymentNode != null) {
            children.add(deploymentNode);
        }
        return deploymentNode;
    }

    /**
     * Gets the DeploymentNode with the specified name.
     *
     * @param name the name of the deployment node
     * @return the DeploymentNode instance with the specified name (or null if it doesn't exist).
     */
    public DeploymentNode getDeploymentNodeWithName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        for (DeploymentNode deploymentNode : getChildren()) {
            if (deploymentNode.getName().equals(name)) {
                return deploymentNode;
            }
        }

        return null;
    }

    /**
     * Adds a relationship between this and another deployment node.
     *
     * @param destination   the destination DeploymentNode
     * @param description   a short description of the relationship
     * @param technology    the technology
     * @return              a Relationship object
     */
    public Relationship uses(DeploymentNode destination, String description, String technology) {
        return uses(destination, description, technology, InteractionStyle.Synchronous);
    }

    /**
     * Adds a relationship between this and another deployment node.
     *
     * @param destination       the destination DeploymentNode
     * @param description       a short description of the relationship
     * @param technology        the technology
     * @param interactionStyle  the interation style (Synchronous vs Asynchronous)
     * @return                  a Relationship object
     */
    public Relationship uses(DeploymentNode destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Gets the set of child deployment nodes.
     *
     * @return  a Set of DeploymentNode objects
     */
    public Set<DeploymentNode> getChildren() {
        return new HashSet<>(children);
    }

    void setChildren(Set<DeploymentNode> children) {
        if (children != null) {
            this.children = new HashSet<>(children);
        }
    }

    /**
     * Gets the set of container instances associated with this deployment node.
     *
     * @return  a Set of ContainerInstance objects
     */
    public Set<ContainerInstance> getContainerInstances() {
        return new HashSet<>(containerInstances);
    }

    void setContainerInstances(Set<ContainerInstance> containerInstances) {
        if (containerInstances != null) {
            this.containerInstances = new HashSet<>(containerInstances);
        }
    }

    /**
     * Gets the parent deployment node.
     *
     * @return  the parent DeploymentNode, or null if there is no parent
     */
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
        // deployment nodes don't have any tags
        return new HashSet<>();
    }

    @Override
    public String getTags() {
        // deployment nodes don't have any tags
        return "";
    }

    @Override
    public String getCanonicalName() {
        if (getParent() != null) {
            return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
        } else {
            return CANONICAL_NAME_SEPARATOR + "Deployment" + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getEnvironment()) + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
        }
    }

}