package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A deployment view, used to show the mapping of container instances to deployment nodes.
 */
public final class DeploymentView extends ModelView implements AnimatedView {

    private Model model;
    private String environment = DeploymentElement.DEFAULT_DEPLOYMENT_ENVIRONMENT;

    @Nonnull
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

        getElements().stream().map(ElementView::getElement).forEach(e -> addNearestNeighbours(e, CustomElement.class));
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
     * Adds an infrastructure node (and its parent deployment nodes) to this view.
     *
     * @param infrastructureNode        the InfrastructureNode to add
     */
    public void add(@Nonnull InfrastructureNode infrastructureNode) {
        addElement(infrastructureNode, true);
        DeploymentNode parent = (DeploymentNode)infrastructureNode.getParent();
        while (parent != null) {
            addElement(parent, true);
            parent = (DeploymentNode)parent.getParent();
        }
    }

    /**
     * Adds a software system instance (and its parent deployment nodes) to this view.
     *
     * @param softwareSystemInstance        the SoftwareSystemInstance to add
     */
    public void add(@Nonnull SoftwareSystemInstance softwareSystemInstance) {
        addElement(softwareSystemInstance, true);
        DeploymentNode parent = (DeploymentNode)softwareSystemInstance.getParent();
        while (parent != null) {
            addElement(parent, true);
            parent = (DeploymentNode)parent.getParent();
        }
    }

    /**
     * Adds a container instance (and its parent deployment nodes) to this view.
     *
     * @param containerInstance     the ContainerInstance to add
     */
    public void add(@Nonnull ContainerInstance containerInstance) {
        addElement(containerInstance, true);
        DeploymentNode parent = (DeploymentNode)containerInstance.getParent();
        while (parent != null) {
            addElement(parent, true);
            parent = (DeploymentNode)parent.getParent();
        }
    }

    /**
     * Removes the given deployment node from this view.
     *
     * @param deploymentNode        the DeploymentNode to be removed
     */
    public void remove(@Nonnull DeploymentNode deploymentNode) {
        for (SoftwareSystemInstance softwareSystemInstance : deploymentNode.getSoftwareSystemInstances()) {
            remove(softwareSystemInstance);
        }

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
     * Removes an infrastructure node from this view.
     *
     * @param infrastructureNode        the InfrastructureNode to be removed
     */
    public void remove(@Nonnull InfrastructureNode infrastructureNode) {
        removeElement(infrastructureNode);
    }

    /**
     * Removes a software system instance from this view.
     *
     * @param softwareSystemInstance     the SoftwareSystemInstance to be removed
     */
    public void remove(@Nonnull SoftwareSystemInstance softwareSystemInstance) {
        removeElement(softwareSystemInstance);
    }

    /**
     * Removes a container instance from this view.
     *
     * @param containerInstance     the ContainerInstance to be removed
     */
    public void remove(@Nonnull ContainerInstance containerInstance) {
        removeElement(containerInstance);
    }

    private boolean addElementInstancesAndDeploymentNodesAndInfrastructureNodes(DeploymentNode deploymentNode, boolean addRelationships) {
        boolean hasElementsOrInfrastructureNodes = false;
        for (SoftwareSystemInstance softwareSystemInstance : deploymentNode.getSoftwareSystemInstances()) {
            try {
                addElement(softwareSystemInstance, addRelationships);
                hasElementsOrInfrastructureNodes = true;
            } catch (ElementNotPermittedInViewException e) {
                // the element can't be added, so ignore it
            }
        }

        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            Container container = containerInstance.getContainer();
            if (getSoftwareSystem() == null || container.getParent().equals(getSoftwareSystem())) {
                try {
                    addElement(containerInstance, addRelationships);
                    hasElementsOrInfrastructureNodes = true;
                } catch (ElementNotPermittedInViewException e) {
                    // the element can't be added, so ignore it
                }
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
    protected void checkElementCanBeAdded(Element elementToBeAdded) {
        if (elementToBeAdded instanceof CustomElement) {
            return;
        }

        if (!(elementToBeAdded instanceof DeploymentElement)) {
            throw new ElementNotPermittedInViewException("Only deployment nodes, infrastructure nodes, software system instances, and container instances can be added to deployment views.");
        }

        DeploymentElement deploymentElementToBeAdded = (DeploymentElement) elementToBeAdded;
        if (!deploymentElementToBeAdded.getEnvironment().equals(this.getEnvironment())) {
            throw new ElementNotPermittedInViewException("Only elements in the " + this.getEnvironment() + " deployment environment can be added to this view.");
        }

        if (this.getSoftwareSystem() != null && elementToBeAdded instanceof SoftwareSystemInstance) {
            SoftwareSystemInstance ssi = (SoftwareSystemInstance) elementToBeAdded;

            if (ssi.getSoftwareSystem().equals(this.getSoftwareSystem())) {
                // adding an instance of the scoped software system isn't permitted
                throw new ElementNotPermittedInViewException("The software system in scope cannot be added to a deployment view.");
            }
        }

        if (elementToBeAdded instanceof SoftwareSystemInstance) {
            // check that a child container instance hasn't been added already
            SoftwareSystemInstance softwareSystemInstanceToBeAdded = (SoftwareSystemInstance) elementToBeAdded;
            Set<String> softwareSystemIds = getElements().stream().map(ElementView::getElement).filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).map(ci -> ci.getContainer().getSoftwareSystem().getId()).collect(Collectors.toSet());

            if (softwareSystemIds.contains(softwareSystemInstanceToBeAdded.getSoftwareSystemId())) {
                throw new ElementNotPermittedInViewException("A child of " + elementToBeAdded.getName() + " is already in this view.");
            }
        }

        if (elementToBeAdded instanceof ContainerInstance) {
            // check that the parent software system instance hasn't been added already
            ContainerInstance containerInstanceToBeAdded = (ContainerInstance)elementToBeAdded;
            Set<String> softwareSystemIds = getElements().stream().map(ElementView::getElement).filter(e -> e instanceof SoftwareSystemInstance).map(e -> (SoftwareSystemInstance)e).map(SoftwareSystemInstance::getSoftwareSystemId).collect(Collectors.toSet());

            if (softwareSystemIds.contains(containerInstanceToBeAdded.getContainer().getSoftwareSystem().getId())) {
                throw new ElementNotPermittedInViewException("A parent of " + elementToBeAdded.getName() + " is already in this view.");
            }
        }
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
     * Adds an animation step, with the specified element instances.
     *
     * @param elementInstances      the element instances that should be shown in the animation step
     */
    public void addAnimation(StaticStructureElementInstance... elementInstances) {
        if (elementInstances == null || elementInstances.length == 0) {
            throw new IllegalArgumentException("One or more software system/container instances must be specified.");
        }

        addAnimation(elementInstances, new InfrastructureNode[0]);
    }

    /**
     * Adds an animation step, with the specified container instances and infrastructure nodes.
     *
     * @param elementInstances          the element instances that should be shown in the animation step
     * @param infrastructureNodes       the container infrastructure nodes that should be shown in the animation step
     */
    public void addAnimation(StaticStructureElementInstance[] elementInstances, InfrastructureNode[] infrastructureNodes) {
        if ((elementInstances == null || elementInstances.length == 0) && (infrastructureNodes == null || infrastructureNodes.length == 0)) {
            throw new IllegalArgumentException("One or more software system/container instances and/or infrastructure nodes must be specified.");
        }

        List<Element> elements = new ArrayList<>();
        if (elementInstances != null) {
            Collections.addAll(elements, elementInstances);
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

    @Nonnull
    @Override
    public List<Animation> getAnimations() {
        return new ArrayList<>(animations);
    }

    void setAnimations(@Nullable List<Animation> animations) {
        if (animations != null) {
            this.animations = new ArrayList<>(animations);
        } else {
            this.animations = new ArrayList<>();
        }
    }

    /**
     * Adds the given custom element to this view, including relationships to/from that custom element.
     *
     * @param customElement the CustomElement to add
     */
    public void add(@Nonnull CustomElement customElement) {
        add(customElement, true);
    }

    /**
     * Adds the given custom element to this view.
     *
     * @param customElement the CustomElement to add
     * @param addRelationships  whether to add relationships to/from the custom element
     */
    public void add(@Nonnull CustomElement customElement, boolean addRelationships) {
        addElement(customElement, addRelationships);
    }

    /**
     * Removes the given custom element from this view.
     *
     * @param customElement the CustomElement to add
     */
    public void remove(@Nonnull CustomElement customElement) {
        removeElement(customElement);
    }

}
