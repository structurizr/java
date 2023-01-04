package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.*;

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

    private String technology;
    private String instances = "1";

    private Set<DeploymentNode> children = new HashSet<>();
    private Set<InfrastructureNode> infrastructureNodes = new HashSet<>();
    private Set<SoftwareSystemInstance> softwareSystemInstances = new HashSet<>();
    private Set<ContainerInstance> containerInstances = new HashSet<>();

    /**
     * Adds a software system instance to this deployment node, replicating relationships.
     *
     * @param softwareSystem        the SoftwareSystem to add an instance of
     * @return a SoftwareSystemInstance object
     */
    public SoftwareSystemInstance add(SoftwareSystem softwareSystem) {
        return add(softwareSystem, DEFAULT_DEPLOYMENT_GROUP);
    }

    /**
     * Adds a software system instance to this deployment node, replicating relationships.
     *
     * @param softwareSystem        the SoftwareSystem to add an instance of
     * @param deploymentGroups      the deployment group(s)
     * @return a SoftwareSystemInstance object
     */
    public SoftwareSystemInstance add(SoftwareSystem softwareSystem, String... deploymentGroups) {
        SoftwareSystemInstance softwareSystemInstance = getModel().addSoftwareSystemInstance(this, softwareSystem, deploymentGroups);
        this.softwareSystemInstances.add(softwareSystemInstance);

        return softwareSystemInstance;
    }

    /**
     * Adds a container instance to this deployment node, replicating relationships.
     *
     * @param container     the Container to add an instance of
     * @return a ContainerInstance object
     */
    public ContainerInstance add(Container container) {
        return add(container, DEFAULT_DEPLOYMENT_GROUP);
    }

    /**
     * Adds a container instance to this deployment node, optionally replicating relationships.
     *
     * @param container                         the Container to add an instance of
     * @param deploymentGroups                  the deployment group(s)
     * @return a ContainerInstance object
     */
    public ContainerInstance add(Container container, String... deploymentGroups) {
        ContainerInstance containerInstance = getModel().addContainerInstance(this, container, deploymentGroups);
        this.containerInstances.add(containerInstance);

        return containerInstance;
    }

    /**
     * Adds a child deployment node.
     *
     * @param name          the name of the deployment node
     * @return  a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name) {
        return addDeploymentNode(name, null, null);
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
     * Gets the infrastructure node with the specified name.
     *
     * @param name      the name of the infrastructure node
     * @return          the InfrastructureNode instance with the specified name (or null if it doesn't exist).
     */
    public InfrastructureNode getInfrastructureNodeWithName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        for (InfrastructureNode infrastructureNode : getInfrastructureNodes()) {
            if (infrastructureNode.getName().equals(name)) {
                return infrastructureNode;
            }
        }

        return null;
    }

    /**
     * Adds a child infrastructure node.
     *
     * @param name          the name of the infrastructure node
     * @return              an InfrastructureNode object
     */
    public InfrastructureNode addInfrastructureNode(String name) {
        return addInfrastructureNode(name, null, null);
    }

    /**
     * Adds a child infrastructure node.
     *
     * @param name          the name of the infrastructure node
     * @param description   a short description
     * @param technology    the technology
     * @return              an InfrastructureNode object
     */
    public InfrastructureNode addInfrastructureNode(String name, String description, String technology) {
        return addInfrastructureNode(name, description, technology, null);
    }

    /**
     * Adds a child infrastructure node.
     *
     * @param name          the name of the infrastructure node
     * @param description   a short description
     * @param technology    the technology
     * @param properties    a Map (String,String) describing name=value properties
     * @return              an InfrastructureNode object
     */
    public InfrastructureNode addInfrastructureNode(String name, String description, String technology, Map<String, String> properties) {
        InfrastructureNode infrastructureNode = getModel().addInfrastructureNode(this, name, description, technology, properties);
        if (infrastructureNode != null) {
            infrastructureNodes.add(infrastructureNode);
        }
        return infrastructureNode;
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
        return uses(destination, description, technology, null);
    }

    /**
     * Adds a relationship between this and another deployment node.
     *
     * @param destination       the destination DeploymentNode
     * @param description       a short description of the relationship
     * @param technology        the technology
     * @param interactionStyle  the interaction style (Synchronous vs Asynchronous)
     * @return                  a Relationship object
     */
    public Relationship uses(DeploymentNode destination, String description, String technology, InteractionStyle interactionStyle) {
        return uses(destination, description, technology, interactionStyle, new String[0]);
    }

    /**
     * Adds a relationship between this and another deployment node.
     *
     * @param destination       the destination DeploymentNode
     * @param description       a short description of the relationship
     * @param technology        the technology
     * @param interactionStyle  the interaction style (Synchronous vs Asynchronous)
     * @param tags              an array of tags
     * @return                  a Relationship object
     */
    public Relationship uses(DeploymentNode destination, String description, String technology, InteractionStyle interactionStyle, String[] tags) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle, tags);
    }

    /**
     * Adds a relationship between this deployment node and an infrastructure node.
     *
     * @param destination   the destination InfrastructureNode
     * @param description   a short description of the relationship
     * @param technology    the technology
     * @return              a Relationship object
     */
    public Relationship uses(InfrastructureNode destination, String description, String technology) {
        return uses(destination, description, technology, null);
    }

    /**
     * Adds a relationship between this deployment node and an infrastructure node.
     *
     * @param destination       the destination InfrastructureNode
     * @param description       a short description of the relationship
     * @param technology        the technology
     * @param interactionStyle  the interaction style (Synchronous vs Asynchronous)
     * @return                  a Relationship object
     */
    public Relationship uses(InfrastructureNode destination, String description, String technology, InteractionStyle interactionStyle) {
        return uses(destination, description, technology, interactionStyle, new String[0]);
    }

    /**
     * Adds a relationship between this deployment node and an infrastructure node.
     *
     * @param destination       the destination InfrastructureNode
     * @param description       a short description of the relationship
     * @param technology        the technology
     * @param interactionStyle  the interaction style (Synchronous vs Asynchronous)
     * @param tags              an array of tags
     * @return                  a Relationship object
     */
    public Relationship uses(InfrastructureNode destination, String description, String technology, InteractionStyle interactionStyle, String[] tags) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle, tags);
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
     * Gets the set of child infrastructure nodes.
     *
     * @return  a Set of InfrastructureNode objects
     */
    public Set<InfrastructureNode> getInfrastructureNodes() {
        return new HashSet<>(infrastructureNodes);
    }

    void setInfrastructureNodes(Set<InfrastructureNode> infrastructureNodes) {
        if (infrastructureNodes != null) {
            this.infrastructureNodes = new HashSet<>(infrastructureNodes);
        }
    }

    @JsonIgnore
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Gets the set of software system instances associated with this deployment node.
     *
     * @return  a Set of SoftwareSystemInstance objects
     */
    public Set<SoftwareSystemInstance> getSoftwareSystemInstances() {
        return new HashSet<>(softwareSystemInstances);
    }

    void setSoftwareSystemInstances(Set<SoftwareSystemInstance> softwareSystemInstances) {
        if (softwareSystemInstances != null) {
            this.softwareSystemInstances = new HashSet<>(softwareSystemInstances);
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

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        setInstances("" + instances);
    }

    @JsonSetter
    public void setInstances(String instances) {
        try {
            int instancesAsInteger = Integer.parseInt(instances);
            if (instancesAsInteger < 1) {
                throw new IllegalArgumentException("Number of instances must be a positive integer or a range.");
            }
        } catch (NumberFormatException nfe) {
            if (instances.matches("\\d*\\.\\.\\d*")) {
                String[] range = instances.split("\\.\\.");
                if (Integer.parseInt(range[0]) > Integer.parseInt(range[1])) {
                    throw new IllegalArgumentException("Range upper bound must be greater than the lower bound.");
                }
            } else if (instances.matches("\\d*..N")) {
                // okay
            } else if (instances.matches("\\d*..\\*")) {
                // okay
            } else {
                throw new IllegalArgumentException("Number of instances must be a positive integer or a range.");
            }
        }

        this.instances = instances;
    }

    @JsonIgnore
    public Set<String> getDefaultTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.DEPLOYMENT_NODE));
    }

    @Override
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

}