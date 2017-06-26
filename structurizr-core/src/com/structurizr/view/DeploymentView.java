package com.structurizr.view;

import com.structurizr.model.*;

import java.util.Set;
import java.util.stream.Collectors;

public class DeploymentView extends View {

    DeploymentView() {
    }

    DeploymentView(SoftwareSystem softwareSystem, String key, String description) {
        super(softwareSystem, key, description);
    }

    /**
     * Adds a deployment node to this view.
     *
     * @param deploymentNode        the DeploymentNode to add
     */
    public void add(DeploymentNode deploymentNode) {
        if (deploymentNode != null) {
            // add all containers within the specified deployment node, if they belong to the software system this view is related to
            if (addContainerInstancesAndDeploymentNodes(deploymentNode)) {
                Element parent = deploymentNode.getParent();
                while (parent != null) {
                    addElement(parent, false);
                    parent = parent.getParent();
                }
            } else {
                throw new IllegalArgumentException("Only deployment nodes with containers belonging to " + getSoftwareSystem().getName() + " can be added to this view.");
            }
        }
    }

    private boolean addContainerInstancesAndDeploymentNodes(DeploymentNode deploymentNode) {
        boolean hasContainers = false;
        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            Container container = containerInstance.getContainer();
            if (container.getParent().equals(getSoftwareSystem())) {
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
     * Removes a deployment node from this view.
     *
     * @param   deploymentNode      the DeploymentNode to remove
     */
    public void remove(DeploymentNode deploymentNode) {
        removeElement(deploymentNode);
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - Deployment";
    }

    public void addAllDeploymentNodes() {
        getModel().getDeploymentNodes().stream().forEach(this::add);
    }

}