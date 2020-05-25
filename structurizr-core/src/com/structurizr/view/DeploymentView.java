package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A deployment view, used to show the mapping of container instances to deployment nodes.
 */
public final class DeploymentView extends View {

    private Model model;
    private String environment;

    private List<Animation> animations = new ArrayList<>();

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
     * Adds a deployment node to this view, including relationships to/from that deployment node (and children).
     *
     * @param deploymentNode        the DeploymentNode to add
     */
    public void add(@Nonnull DeploymentNode deploymentNode) {
        add(deploymentNode, true);
    }

    /**
     * Adds a deployment node to this view.
     *
     * @param deploymentNode    the DeploymentNode to add
     * @param addRelationships  whether to add relationships to/from the person
     */
    public void add(@Nonnull DeploymentNode deploymentNode, boolean addRelationships) {
        if (deploymentNode == null) {
            throw new IllegalArgumentException("A deployment node must be specified.");
        }

        if (addContainerInstancesAndDeploymentNodesAndInfrastructureNodes(deploymentNode, addRelationships)) {
            Element parent = deploymentNode.getParent();
            while (parent != null) {
                addElement(parent, addRelationships);
                parent = parent.getParent();
            }
        }
    }

    private boolean addContainerInstancesAndDeploymentNodesAndInfrastructureNodes(DeploymentNode deploymentNode, boolean addRelationships) {
        boolean hasContainersOrInfrastructureNodes = false;
        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            Container container = containerInstance.getContainer();
            if (getSoftwareSystem() == null || container.getParent().equals(getSoftwareSystem())) {
                addElement(containerInstance, addRelationships);
                hasContainersOrInfrastructureNodes = true;
            }
        }

        for (InfrastructureNode infrastructureNode : deploymentNode.getInfrastructureNodes()) {
            addElement(infrastructureNode, addRelationships);
            hasContainersOrInfrastructureNodes = true;
        }

        for (DeploymentNode child : deploymentNode.getChildren()) {
            hasContainersOrInfrastructureNodes = hasContainersOrInfrastructureNodes | addContainerInstancesAndDeploymentNodesAndInfrastructureNodes(child, addRelationships);
        }

        if (hasContainersOrInfrastructureNodes) {
            addElement(deploymentNode, addRelationships);
        }

        return hasContainersOrInfrastructureNodes;
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

    /**
     * Adds an animation step, with the specified container instances.
     *
     * @param containerInstances        the container instances that should be shown in the animation step
     */
    public void addAnimation(ContainerInstance... containerInstances) {
        if (containerInstances == null || containerInstances.length == 0) {
            throw new IllegalArgumentException("One or more container instances must be specified.");
        }

        Set<String> elementIdsInPreviousAnimationSteps = new HashSet<>();
        for (Animation animationStep : animations) {
            elementIdsInPreviousAnimationSteps.addAll(animationStep.getElements());
        }

        Set<Element> elementsInThisAnimationStep = new HashSet<>();
        Set<Relationship> relationshipsInThisAnimationStep = new HashSet<>();

        for (ContainerInstance containerInstance : containerInstances) {
            if (isElementInView(containerInstance) && !elementIdsInPreviousAnimationSteps.contains(containerInstance.getId())) {
                elementIdsInPreviousAnimationSteps.add(containerInstance.getId());
                elementsInThisAnimationStep.add(containerInstance);

                Element deploymentNode = findDeploymentNode(containerInstance);
                while (deploymentNode != null) {
                    if (!elementIdsInPreviousAnimationSteps.contains(deploymentNode.getId())) {
                        elementIdsInPreviousAnimationSteps.add(deploymentNode.getId());
                        elementsInThisAnimationStep.add(deploymentNode);
                    }

                    deploymentNode = deploymentNode.getParent();
                }
            }
        }

        if (elementsInThisAnimationStep.size() == 0) {
            throw new IllegalArgumentException("None of the specified container instances exist in this view.");
        }

        for (RelationshipView relationshipView : this.getRelationships()) {
            if (
                    (elementsInThisAnimationStep.contains(relationshipView.getRelationship().getSource()) && elementIdsInPreviousAnimationSteps.contains(relationshipView.getRelationship().getDestination().getId())) ||
                            (elementIdsInPreviousAnimationSteps.contains(relationshipView.getRelationship().getSource().getId()) && elementsInThisAnimationStep.contains(relationshipView.getRelationship().getDestination()))
            ) {
                relationshipsInThisAnimationStep.add(relationshipView.getRelationship());
            }
        }

        animations.add(new Animation(animations.size() + 1, elementsInThisAnimationStep, relationshipsInThisAnimationStep));
    }

    private DeploymentNode findDeploymentNode(ContainerInstance containerInstance) {
        for (Element element : getModel().getElements()) {
            if (element instanceof DeploymentNode) {
                DeploymentNode deploymentNode = (DeploymentNode)element;
                if (deploymentNode.getContainerInstances().contains(containerInstance)) {
                    return deploymentNode;
                }
            }
        }

        return null;
    }

    public List<Animation> getAnimations() {
        return new ArrayList<>(animations);
    }

    void setAnimations(List<Animation> animations) {
        if (animations != null) {
            this.animations = new ArrayList<>(animations);
        } else {
            this.animations = new ArrayList<>();
        }
    }

}