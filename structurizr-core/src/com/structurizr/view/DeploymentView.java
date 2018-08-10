package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;

/**
 * A deployment view, used to show the mapping of container instances to deployment nodes.
 */
public final class DeploymentView extends View {

    private Model model;
    private String environment;

    DeploymentView() {
    }

    DeploymentView(Model model, String key, String description) {
        super(null, key, description);

        this.model = model;
    }

    DeploymentView(SoftwareSystem softwareSystem, String key, String description) {
        super(softwareSystem, key, description);

        this.model = softwareSystem.getModel();
    }

    @JsonIgnore
    @Override
    public Model getModel() {
        return this.model;
    }

    void setModel(Model model) {
        this.model = model;
    }

    /**
     * Adds all of the top-level deployment nodes to this view.
     */
    public void addAllDeploymentNodes() {
        getModel().getDeploymentNodes().forEach(this::add);
    }

    /**
     * Adds a deployment node to this view.
     *
     * @param deploymentNode        the DeploymentNode to add
     */
    public void add(@Nonnull DeploymentNode deploymentNode) {
        if (deploymentNode == null) {
            throw new IllegalArgumentException("A deployment node must be specified.");
        }

        if (addContainerInstancesAndDeploymentNodes(deploymentNode)) {
            Element parent = deploymentNode.getParent();
            while (parent != null) {
                addElement(parent, false);
                parent = parent.getParent();
            }
        }
    }

    private boolean addContainerInstancesAndDeploymentNodes(DeploymentNode deploymentNode) {
        boolean hasContainers = false;
        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            Container container = containerInstance.getContainer();
            if (getSoftwareSystem() == null || container.getParent().equals(getSoftwareSystem())) {
                addElement(containerInstance, true);
                hasContainers = true;
            }
        }

        for (DeploymentNode child : deploymentNode.getChildren()) {
            hasContainers = hasContainers | addContainerInstancesAndDeploymentNodes(child);
        }

        if (hasContainers) {
            addElement(deploymentNode, false);
        }

        return hasContainers;
    }

    /**
     * Adds a Relationship to this view.
     *
     * @param relationship  the Relationship to be added
     * @return  a RelationshipView object representing the relationship added
     */
    public RelationshipView add(@Nonnull Relationship relationship) {
        return addRelationship(relationship);
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return  the name, as a String
     */
    @Override
    public String getName() {
        String name;
        if (getSoftwareSystem() != null) {
            name = getSoftwareSystem().getName() + " - Deployment";
        } else {
            name = "Deployment";
        }

        if (!StringUtils.isNullOrEmpty(getEnvironment())) {
            name = name + " - " + getEnvironment();
        }

        return name;
    }

    /**
     * Gets the name of the environment that this deployment view is for (e.g. "Development", "Live", etc).
     *
     * @return  the environment name, as a String
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * Sets the name of the environment that this deployment view is for (e.g. "Development", "Live", etc).
     *
     * @param environment       the environment name, as a String
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    protected boolean canBeRemoved(Element element) {
        return true;
    }

}