package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.*;

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
     * Adds the default set of elements to this view.
     */
    public void addDefaultElements() {
        addAllDeploymentNodes();
    }

    /**
     * Adds all of the top-level deployment nodes to this view, for the same deployment environment (if set).
     */
    public void addAllDeploymentNodes() {
        for (DeploymentNode deploymentNode : getModel().getDeploymentNodes()) {
            if (deploymentNode.getParent() == null) {
                if (this.getEnvironment() == null || this.getEnvironment().equals(deploymentNode.getEnvironment())) {
                    add(deploymentNode);
                }
            }
        }
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

        if (addElementInstancesAndDeploymentNodesAndInfrastructureNodes(deploymentNode, addRelationships)) {
            Element parent = deploymentNode.getParent();
            while (parent != null) {
                addElement(parent, addRelationships);
                parent = parent.getParent();
            }
        }
    }

    /**
     * Removes the given deployment node from this view.
     *
     * @param deploymentNode        the DeploymentNode to be removed
     */
    public void remove(@Nonnull DeploymentNode deploymentNode) {
        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            remove(containerInstance);
        }

        for (InfrastructureNode infrastructureNode : deploymentNode.getInfrastructureNodes()) {
            remove(infrastructureNode);
        }

        for (DeploymentNode child : deploymentNode.getChildren()) {
            remove(child);
        }

        removeElement(deploymentNode);
    }

    /**
     * Removes the given infrastructure node from this view.
     *
     * @param infrastructureNode        the InfrastructureNode to be removed
     */
    public void remove(@Nonnull InfrastructureNode infrastructureNode) {
        removeElement(infrastructureNode);
    }

    /**
     * Removes the given infrastructure node from this view.
     *
     * @param containerInstance     the ContainerInstance to be removed
     */
    public void remove(@Nonnull ContainerInstance containerInstance) {
        removeElement(containerInstance);
    }

    private boolean addElementInstancesAndDeploymentNodesAndInfrastructureNodes(DeploymentNode deploymentNode, boolean addRelationships) {
        boolean hasElementsOrInfrastructureNodes = false;
        for (SoftwareSystemInstance softwareSystemInstance : deploymentNode.getSoftwareSystemInstances()) {
            addElement(softwareSystemInstance, addRelationships);
            hasElementsOrInfrastructureNodes = true;
        }

        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            Container container = containerInstance.getContainer();
            if (getSoftwareSystem() == null || container.getParent().equals(getSoftwareSystem())) {
                addElement(containerInstance, addRelationships);
                hasElementsOrInfrastructureNodes = true;
            }
        }

        for (InfrastructureNode infrastructureNode : deploymentNode.getInfrastructureNodes()) {
            addElement(infrastructureNode, addRelationships);
            hasElementsOrInfrastructureNodes = true;
        }

        for (DeploymentNode child : deploymentNode.getChildren()) {
            hasElementsOrInfrastructureNodes = hasElementsOrInfrastructureNodes | addElementInstancesAndDeploymentNodesAndInfrastructureNodes(child, addRelationships);
        }

        if (hasElementsOrInfrastructureNodes) {
            addElement(deploymentNode, addRelationships);
        }

        return hasElementsOrInfrastructureNodes;
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
     * Adds an animation step, with the specified infrastructure nodes.
     *
     * @param infrastructureNodes        the infrastructure nodes that should be shown in the animation step
     */
    public void addAnimation(InfrastructureNode... infrastructureNodes) {
        if (infrastructureNodes == null || infrastructureNodes.length == 0) {
            throw new IllegalArgumentException("One or more infrastructure nodes must be specified.");
        }

        addAnimation(new ContainerInstance[0], infrastructureNodes);
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

        addAnimation(containerInstances, new InfrastructureNode[0]);
    }

    /**
     * Adds an animation step, with the specified container instances and infrastructure nodes.
     *
     * @param containerInstances      the container instances that should be shown in the animation step
     * @param infrastructureNodes      the container infrastructure nodes that should be shown in the animation step
     */
    public void addAnimation(ContainerInstance[] containerInstances, InfrastructureNode[] infrastructureNodes) {
        if ((containerInstances == null || containerInstances.length == 0) && (infrastructureNodes == null || infrastructureNodes.length == 0)) {
            throw new IllegalArgumentException("One or more container instances/infrastructure nodes must be specified.");
        }

        List<Element> elements = new ArrayList<>();
        if (containerInstances != null) {
            Collections.addAll(elements, containerInstances);
        }
        if (infrastructureNodes != null) {
            Collections.addAll(elements, infrastructureNodes);
        }

        addAnimationStep(elements.toArray(new Element[0]));
    }

    private void addAnimationStep(Element... elements) {

        Set<String> elementIdsInPreviousAnimationSteps = new HashSet<>();
        for (Animation animationStep : animations) {
            elementIdsInPreviousAnimationSteps.addAll(animationStep.getElements());
        }

        Set<Element> elementsInThisAnimationStep = new HashSet<>();
        Set<Relationship> relationshipsInThisAnimationStep = new HashSet<>();

        for (Element element : elements) {
            if (isElementInView(element) && !elementIdsInPreviousAnimationSteps.contains(element.getId())) {
                elementIdsInPreviousAnimationSteps.add(element.getId());
                elementsInThisAnimationStep.add(element);

                Element deploymentNode = findDeploymentNode(element);
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

    private DeploymentNode findDeploymentNode(Element e) {
        for (Element element : getModel().getElements()) {
            if (element instanceof DeploymentNode) {
                DeploymentNode deploymentNode = (DeploymentNode) element;

                if (e instanceof ContainerInstance) {
                    if (deploymentNode.getContainerInstances().contains(e)) {
                        return deploymentNode;
                    }
                }

                if (e instanceof InfrastructureNode) {
                    if (deploymentNode.getInfrastructureNodes().contains(e)) {
                        return deploymentNode;
                    }
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