package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.PropertyHolder;
import com.structurizr.WorkspaceValidationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a software architecture model, into which all model elements are added.
 */
public final class Model implements PropertyHolder {

    private IdGenerator idGenerator = new SequentialIntegerIdGeneratorStrategy();

    private final Map<String, Element> elementsById = new HashMap<>();
    private final Map<String, Relationship> relationshipsById = new HashMap<>();

    private Enterprise enterprise;

    private Set<Person> people = new LinkedHashSet<>();
    private Set<SoftwareSystem> softwareSystems = new LinkedHashSet<>();
    private Set<DeploymentNode> deploymentNodes = new LinkedHashSet<>();
    private Set<CustomElement> customElements = new LinkedHashSet<>();

    private ImpliedRelationshipsStrategy impliedRelationshipsStrategy = new DefaultImpliedRelationshipsStrategy();

    private Map<String, String> properties = new HashMap<>();

    Model() {
    }

    /**
     * Gets the enterprise associated with this model.
     *
     * @return an Enterprise instance, or null if one has not been set
     */
    public Enterprise getEnterprise() {
        return enterprise;
    }

    /**
     * Sets the enterprise associated with this model.
     * This is typically used in conjunction with {@link Location}.
     *
     * @param enterprise an Enterprise instance
     */
    @Deprecated
    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    /**
     * Creates a software system (with an unspecified location) and adds it to the model.
     *
     * @param name        the name of the software system
     * @return the SoftwareSystem instance created and added to the model (or null)
     * @throws IllegalArgumentException if a software system with the same name already exists
     */
    public SoftwareSystem addSoftwareSystem(@Nonnull String name) {
        return addSoftwareSystem(name, "");
    }

    /**
     * Creates a software system and adds it to the model.
     *
     * @param name        the name of the software system
     * @param description a short description of the software system
     * @return the SoftwareSystem instance created and added to the model (or null)
     * @throws IllegalArgumentException if a software system with the same name already exists
     */
    public SoftwareSystem addSoftwareSystem(@Nonnull String name, @Nullable String description) {
        if (getSoftwareSystemWithName(name) == null) {
            SoftwareSystem softwareSystem = new SoftwareSystem();
            softwareSystem.setLocation(Location.Unspecified);
            softwareSystem.setName(name);
            softwareSystem.setDescription(description);

            softwareSystems.add(softwareSystem);

            softwareSystem.setId(idGenerator.generateId(softwareSystem));
            addElementToInternalStructures(softwareSystem);

            return softwareSystem;
        } else {
            throw new IllegalArgumentException("A top-level element named '" + name + "' already exists.");
        }
    }

    /**
     * Creates a software system and adds it to the model.
     *
     * @param location    the location of the software system (e.g. internal, external, etc)
     * @param name        the name of the software system
     * @param description a short description of the software system
     * @return the SoftwareSystem instance created and added to the model (or null)
     * @throws IllegalArgumentException if a software system with the same name already exists
     */
    @Nonnull
    @Deprecated
    public SoftwareSystem addSoftwareSystem(@Nullable Location location, @Nonnull String name, @Nullable String description) {
        if (getSoftwareSystemWithName(name) == null) {
            SoftwareSystem softwareSystem = new SoftwareSystem();
            softwareSystem.setLocation(location);
            softwareSystem.setName(name);
            softwareSystem.setDescription(description);

            softwareSystems.add(softwareSystem);

            softwareSystem.setId(idGenerator.generateId(softwareSystem));
            addElementToInternalStructures(softwareSystem);

            return softwareSystem;
        } else {
            throw new IllegalArgumentException("A top-level element named '" + name + "' already exists.");
        }
    }

    /**
     * Creates a person (with an unspecified location) and adds it to the model.
     *
     * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @return the Person instance created and added to the model (or null)
     * @throws IllegalArgumentException if a person with the same name already exists
     */
    @Nonnull
    public Person addPerson(@Nonnull String name) {
        return addPerson(name, "");
    }

    /**
     * Creates a person and adds it to the model.
     *
     * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description a short description of the person
     * @return the Person instance created and added to the model (or null)
     * @throws IllegalArgumentException if a person with the same name already exists
     */
    @Nonnull
    public Person addPerson(@Nonnull String name, @Nullable String description) {
        if (getPersonWithName(name) == null) {
            Person person = new Person();
            person.setLocation(Location.Unspecified);
            person.setName(name);
            person.setDescription(description);

            people.add(person);

            person.setId(idGenerator.generateId(person));
            addElementToInternalStructures(person);

            return person;
        } else {
            throw new IllegalArgumentException("A top-level element named '" + name + "' already exists.");
        }
    }

    /**
     * Creates a person and adds it to the model.
     *
     * @param location    the location of the person (e.g. internal, external, etc)
     * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description a short description of the person
     * @return the Person instance created and added to the model (or null)
     * @throws IllegalArgumentException if a person with the same name already exists
     */
    @Nonnull
    @Deprecated
    public Person addPerson(Location location, @Nonnull String name, @Nullable String description) {
        if (getPersonWithName(name) == null) {
            Person person = new Person();
            person.setLocation(location);
            person.setName(name);
            person.setDescription(description);

            people.add(person);

            person.setId(idGenerator.generateId(person));
            addElementToInternalStructures(person);

            return person;
        } else {
            throw new IllegalArgumentException("A top-level element named '" + name + "' already exists.");
        }
    }

    /**
     * Creates a custom element and adds it to the model.
     *
     * @param name        the name of the custom element
     * @return the CustomElement instance created and added to the model (or null)
     * @throws IllegalArgumentException if a custom element/person/software system with the same name already exists
     */
    @Nonnull
    public CustomElement addCustomElement(@Nonnull String name) {
        return addCustomElement(name, "", "");
    }

    /**
     * Creates a custom element and adds it to the model.
     *
     * @param name          the name of the custom element
     * @param description   a short description of the custom element
     * @param metadata      the metadata of the custom element
     * @return the CustomElement instance created and added to the model (or null)
     * @throws IllegalArgumentException if a custom element/person/software system with the same name already exists
     */
    @Nonnull
    public CustomElement addCustomElement(@Nonnull String name, @Nullable String metadata, @Nullable String description) {
        if (getCustomElementWithName(name) == null) {
            CustomElement customElement = new CustomElement();
            customElement.setName(name);
            customElement.setMetadata(metadata);
            customElement.setDescription(description);

            customElements.add(customElement);

            customElement.setId(idGenerator.generateId(customElement));
            addElementToInternalStructures(customElement);

            return customElement;
        } else {
            throw new IllegalArgumentException("A top-level element named '" + name + "' already exists.");
        }
    }



    @Nonnull
    Container addContainer(SoftwareSystem parent, @Nonnull String name, @Nullable String description, @Nullable String technology) {
        if (parent.getContainerWithName(name) == null) {
            Container container = new Container();
            container.setName(name);
            container.setDescription(description);
            container.setTechnology(technology);

            container.setParent(parent);
            parent.add(container);

            container.setId(idGenerator.generateId(container));
            addElementToInternalStructures(container);

            return container;
        } else {
            throw new IllegalArgumentException("A container named '" + name + "' already exists for this software system.");
        }
    }

    Component addComponent(Container parent, String name, String description, String technology) {
        if (parent.getComponentWithName(name) == null) {
            Component component = new Component();
            component.setName(name);
            component.setDescription(description);
            component.setTechnology(technology);

            component.setParent(parent);
            parent.add(component);

            component.setId(idGenerator.generateId(component));
            addElementToInternalStructures(component);

            return component;
        } else {
            throw new IllegalArgumentException("A component named '" + name + "' already exists for this container.");
        }
    }

    @Nullable
    Relationship addRelationship(Element source, @Nonnull Element destination, String description, String technology, boolean createImpliedRelationships) {
        return addRelationship(source, destination, description, technology, null, new String[0], createImpliedRelationships);
    }

    @Nullable
    Relationship addRelationship(Element source, @Nonnull Element destination, String description, String technology, InteractionStyle interactionStyle) {
        return addRelationship(source, destination, description, technology, interactionStyle, new String[0], true);
    }

    @Nullable
    Relationship addRelationship(Element source, @Nonnull Element destination, String description, String technology, InteractionStyle interactionStyle, String[] tags) {
        return addRelationship(source, destination, description, technology, interactionStyle, tags, true);
    }

    @Nullable
    Relationship addRelationship(Element source, @Nonnull Element destination, String description, String technology, InteractionStyle interactionStyle, String[] tags, boolean createImpliedRelationships) {
        if (destination == null) {
            throw new IllegalArgumentException("The destination must be specified.");
        }

        if (isChildOf(source, destination) || isChildOf(destination, source)) {
            throw new IllegalArgumentException("Relationships cannot be added between parents and children.");
        }

        Relationship relationship = new Relationship(source, destination, description, technology, interactionStyle, tags);

        if (addRelationship(relationship)) {

            if (createImpliedRelationships) {
                if
                (
                    (source instanceof CustomElement || source instanceof Person || source instanceof SoftwareSystem || source instanceof Container || source instanceof Component) &&
                    (destination instanceof CustomElement || destination instanceof Person || destination instanceof SoftwareSystem || destination instanceof Container || destination instanceof Component)
                ) {
                    impliedRelationshipsStrategy.createImpliedRelationships(relationship);
                }
            }

            return relationship;
        }

        return null;
    }

    private boolean isChildOf(Element e1, Element e2) {
        if (e1 instanceof Person || e2 instanceof Person) {
            return false;
        }

        Element parent = e2.getParent();
        while (parent != null) {
            if (parent.getId().equals(e1.getId())) {
                return true;
            }

            parent = parent.getParent();
        }

        return false;
    }

    private boolean addRelationship(Relationship relationship) {
        if (!relationship.getSource().has(relationship)) {
            relationship.setId(idGenerator.generateId(relationship));
            relationship.getSource().addRelationship(relationship);

            addRelationshipToInternalStructures(relationship);
            return true;
        } else {
            return false;
        }
    }

    private void addElementToInternalStructures(Element element) {
        // check that the ID is unique
        if (getElement(element.getId()) != null || getRelationship(element.getId()) != null) {
            throw new WorkspaceValidationException("The element " + element.getCanonicalName() + " has a non-unique ID of " + element.getId() + ".");
        }

        elementsById.put(element.getId(), element);
        element.setModel(this);
        idGenerator.found(element.getId());
    }

    private void addRelationshipToInternalStructures(Relationship relationship) {
        // check that the ID is unique
        if (getElement(relationship.getId()) != null || getRelationship(relationship.getId()) != null) {
            throw new WorkspaceValidationException("The relationship " + relationship.toString() + " has a non-unique ID of " + relationship.getId() + ".");
        }

        relationshipsById.put(relationship.getId(), relationship);
        relationship.setModel(this);
        idGenerator.found(relationship.getId());
    }

    /**
     * Gets the set of all elements in this model.
     *
     * @return a Set of Element instances
     */
    @JsonIgnore
    @Nonnull
    public Set<Element> getElements() {
        return new HashSet<>(this.elementsById.values());
    }

    /**
     * Gets the element with the specified ID.
     *
     * @param id the {@link Element#getId()} of the element
     * @return the element in this model with the specified ID (or null if it doesn't exist)
     * @see Element#getId()
     */
    @Nullable
    public Element getElement(@Nonnull String id) {
        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("An element ID must be specified.");
        }

        return elementsById.get(id);
    }

    /**
     * Gets the set of all relationships in this model.
     *
     * @return a Set of Relationship objects
     */
    @JsonIgnore
    @Nonnull
    public Set<Relationship> getRelationships() {
        return new HashSet<>(this.relationshipsById.values());
    }

    /**
     * Gets the relationship with the specified ID.
     *
     * @param id the {@link Relationship#getId()} of the relationship
     * @return the relationship in this model with the specified ID (or null if it doesn't exist).
     * @see Relationship#getId()
     */
    @Nullable
    public Relationship getRelationship(@Nonnull String id) {
        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("A relationship ID must be specified.");
        }

        return relationshipsById.get(id);
    }

    /**
     * Gets the set of all custom elements in this model.
     *
     * @return a Set of CustomElement instances
     */
    @Nonnull
    public Set<CustomElement> getCustomElements() {
        return new LinkedHashSet<>(customElements);
    }

    void setCustomElements(Set<CustomElement> customElements) {
        if (customElements != null) {
            this.customElements = new LinkedHashSet<>(customElements);
        }
    }

    /**
     * Gets the set of all people in this model.
     *
     * @return a Set of Person instances
     */
    @Nonnull
    public Set<Person> getPeople() {
        return new LinkedHashSet<>(people);
    }

    void setPeople(Set<Person> people) {
        if (people != null) {
            this.people = new LinkedHashSet<>(people);
        }
    }

    /**
     * Gets the set of all software systems in this model.
     *
     * @return a Set of SoftwareSystem instances
     */
    @Nonnull
    public Set<SoftwareSystem> getSoftwareSystems() {
        return new LinkedHashSet<>(softwareSystems);
    }

    void setSoftwareSystems(Set<SoftwareSystem> softwareSystems) {
        if (softwareSystems != null) {
            this.softwareSystems = new LinkedHashSet<>(softwareSystems);
        }
    }

    /**
     * Gets the set of all top-level deployment nodes in this model.
     *
     * @return a Set of DeploymentNode instances
     */
    @Nonnull
    public Set<DeploymentNode> getDeploymentNodes() {
        return new LinkedHashSet<>(deploymentNodes);
    }

    void setDeploymentNodes(Set<DeploymentNode> deploymentNodes) {
        if (deploymentNodes != null) {
            this.deploymentNodes = new LinkedHashSet<>(deploymentNodes);
        }
    }

    void hydrate() {
        // add all of the elements to the model
        customElements.forEach(this::addElementToInternalStructures);
        people.forEach(this::addElementToInternalStructures);

        for (SoftwareSystem softwareSystem : softwareSystems) {
            addElementToInternalStructures(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                addElementToInternalStructures(container);
                container.setParent(softwareSystem);
                for (Component component : container.getComponents()) {
                    addElementToInternalStructures(component);
                    component.setParent(container);
                }
            }
        }

        deploymentNodes.forEach(dn -> hydrateDeploymentNode(dn, null));

        // now hydrate the relationships
        getElements().forEach(this::hydrateRelationships);

        // now check all of the element names are unique
        Collection<Element> peopleAndSoftwareSystems = new ArrayList<>();
        peopleAndSoftwareSystems.addAll(people);
        peopleAndSoftwareSystems.addAll(softwareSystems);
        for (Element element : peopleAndSoftwareSystems) {
            checkNameIsUnique(peopleAndSoftwareSystems, element.getName(), "A person or software system named \"%s\" already exists.");
        }

        for (SoftwareSystem softwareSystem : softwareSystems) {
            for (Container container : softwareSystem.getContainers()) {
                checkNameIsUnique(softwareSystem.getContainers(), container.getName(), "A container named \"%s\" already exists within \"" + softwareSystem.getName() + "\".");

                for (Component component : container.getComponents()) {
                    checkNameIsUnique(container.getComponents(), component.getName(), "A component named \"%s\" already exists within \"" + container.getName() + "\".");
                }
            }
        }

        for (DeploymentNode deploymentNode : deploymentNodes) {
            checkNameIsUnique(deploymentNodes, deploymentNode.getName(), deploymentNode.getEnvironment(), "A top-level deployment node named \"%s\" already exists for the environment named \"" + deploymentNode.getEnvironment() + "\".");

            if (deploymentNode.hasChildren()) {
                checkChildNamesAreUnique(deploymentNode);
            }
        }

        // and check that all relationships are unique
        for (Element element : getElements()) {
            for (Relationship relationship : element.getRelationships()) {
                checkDescriptionIsUnique(element.getRelationships(), relationship);
            }
        }
    }

    private void hydrateDeploymentNode(DeploymentNode deploymentNode, DeploymentNode parent) {
        final String nodeType = parent == null ? "root" : "child";

        deploymentNode.setParent(parent);
        addElementToInternalStructures(deploymentNode);

        deploymentNode.getChildren().forEach(child -> hydrateDeploymentNode(child, deploymentNode));

        verifyDeploymentNode(deploymentNode, nodeType);

        deploymentNode.getSoftwareSystemInstances().forEach(softwareSystemInstance -> {
            Element softwareSystem = getElement(softwareSystemInstance.getSoftwareSystemId());
            softwareSystemInstance.setSoftwareSystem((SoftwareSystem) softwareSystem);
            softwareSystemInstance.setParent(deploymentNode);
            addElementToInternalStructures(softwareSystemInstance);
        });

        deploymentNode.getContainerInstances().forEach(containerInstance -> {
            Element container = getElement(containerInstance.getContainerId());
            containerInstance.setContainer((Container) container);
            containerInstance.setParent(deploymentNode);
            addElementToInternalStructures(containerInstance);
        });

        deploymentNode.getInfrastructureNodes().forEach(infrastructureNode -> {
            infrastructureNode.setParent(deploymentNode);
            addElementToInternalStructures(infrastructureNode);
        });
    }

    private void verifyDeploymentNode(DeploymentNode deploymentNode, String nodeType) {
        // Verify each deployment node Software system instance
        deploymentNode.getSoftwareSystemInstances().forEach(softwareSystemInstance -> {
            Element softwareSystem = getElement(softwareSystemInstance.getSoftwareSystemId());
            if (!(softwareSystem instanceof SoftwareSystem)) {
                // This generally means that the model (e.g. from the DSL editor) has some
                // mistake, we are referencing a software system definition within a deployment
                // node that is missing from the model.
                throw new WorkspaceValidationException(
                        String.format(
                                "SoftwareSystem (id=%s) does not exist in the model (referenced by SofwareSystem Instance in a %s DeploymentNode (name=%s)",
                                softwareSystemInstance.getSoftwareSystemId(), nodeType, deploymentNode.getName()));
            }
        });

        // verify each deployment node Container instance
        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            Element container = getElement(containerInstance.getContainerId());

            if (!(container instanceof Container)) {
                // This generally means that the model (e.g. from the DSL editor) has some
                // mistake, we are referencing a container definition within a deployment node
                // that is missing from the model.
                throw new WorkspaceValidationException(
                        String.format(
                                "Container (id=%s) does not exist in the model (referenced by Container Instance in a %s DeploymentNode (name=%s)",
                                containerInstance.getContainerId(), nodeType, deploymentNode.getName()));
            }
        }
    }

    private void checkNameIsUnique(Collection<? extends Element> elements, String name, String errorMessage) {
        if (elements.stream().filter(e -> e.getName().equals(name)).count() != 1) {
            throw new WorkspaceValidationException(
                    String.format(errorMessage, name));
        }
    }

    private void checkNameIsUnique(Collection<DeploymentNode> deploymentNodes, String name, String environment, String errorMessage) {
        if (deploymentNodes.stream().filter(dn -> dn.getName().equals(name) && dn.getEnvironment().equals(environment)).count() != 1) {
            throw new WorkspaceValidationException(
                    String.format(errorMessage, name));
        }
    }

    private void checkChildNamesAreUnique(DeploymentNode deploymentNode) {
        for (DeploymentNode child : deploymentNode.getChildren()) {
            checkNameIsUnique(deploymentNode.getChildren(), child.getName(), deploymentNode.getEnvironment(), "A deployment node named \"%s\" already exists within \"" + deploymentNode.getName() + "\".");

            if (child.hasChildren()) {
                checkChildNamesAreUnique(child);
            }
        }
    }

    private void checkDescriptionIsUnique(Collection<Relationship> relationships, Relationship relationship) {
        if (relationships.stream().filter(r -> r.getDestination().equals(relationship.getDestination()) && r.getDescription().equals(relationship.getDescription())).count() != 1) {
            throw new WorkspaceValidationException(
                    String.format(
                            "A relationship with the description \"%s\" already exists between \"%s\" and \"%s\".",
                            relationship.getDescription(), relationship.getSource().getName(), relationship.getDestination().getName()));
        }
    }

    private void hydrateRelationships(Element element) {
        for (Relationship relationship : element.getRelationships()) {
            relationship.setSource(getElement(relationship.getSourceId()));
            relationship.setDestination(getElement(relationship.getDestinationId()));
            addRelationshipToInternalStructures(relationship);
        }
    }

    /**
     * Determines whether this model contains the specified element.
     *
     * @param element any element
     * @return true, if the element is contained in this model
     */
    public boolean contains(Element element) {
        return elementsById.values().contains(element);
    }

    /**
     * Gets the software system with the specified name.
     *
     * @param name the name of a {@link SoftwareSystem}
     * @return the SoftwareSystem instance with the specified name (or null if it doesn't exist)
     * @throws IllegalArgumentException if the name is null or empty
     */
    @Nullable
    public SoftwareSystem getSoftwareSystemWithName(@Nonnull String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A software system name must be specified.");
        }

        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getName().equals(name)) {
                return softwareSystem;
            }
        }

        return null;
    }

    /**
     * Gets the software system with the specified ID.
     *
     * @param id the {@link SoftwareSystem#getId()} of the software system
     * @return the SoftwareSystem instance with the specified ID (or null if it doesn't exist).
     * @throws IllegalArgumentException if the id is null or empty
     * @see SoftwareSystem#getId()
     */
    @Nullable
    public SoftwareSystem getSoftwareSystemWithId(@Nonnull String id) {
        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("A software system ID must be specified.");
        }

        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getId().equals(id)) {
                return softwareSystem;
            }
        }

        return null;
    }

    /**
     * Gets the person with the specified name.
     *
     * @param name the name of the person
     * @return the Person instance with the specified name (or null if it doesn't exist)
     * @throws IllegalArgumentException if the name is null or empty
     */
    @Nullable
    public Person getPersonWithName(@Nonnull String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A person name must be specified.");
        }

        for (Person person : getPeople()) {
            if (person.getName().equals(name)) {
                return person;
            }
        }

        return null;
    }

    /**
     * Gets the custom element with the specified name.
     *
     * @param name the name of the custom element
     * @return the CustomElement instance with the specified name (or null if it doesn't exist)
     * @throws IllegalArgumentException if the name is null or empty
     */
    @Nullable
    public CustomElement getCustomElementWithName(@Nonnull String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A custom element name must be specified.");
        }

        for (CustomElement customElement : getCustomElements()) {
            if (customElement.getName().equals(name)) {
                return customElement;
            }
        }

        return null;
    }

    /**
     * Determines whether this model is empty.
     *
     * @return true if the model contains no people, software systems or deployment nodes; false otherwise
     */
    @JsonIgnore
    public boolean isEmpty() {
        return people.isEmpty() && softwareSystems.isEmpty() && deploymentNodes.isEmpty();
    }

    /**
     * Adds a top-level deployment node to this model.
     *
     * @param name        the name of the deployment node
     * @return a DeploymentNode instance
     * @throws IllegalArgumentException if the name is not specified, or a top-level deployment node with the same name already exists in the model
     */
    @Nonnull
    public DeploymentNode addDeploymentNode(@Nonnull String name) {
        return addDeploymentNode(DeploymentNode.DEFAULT_DEPLOYMENT_ENVIRONMENT, name, null, null);
    }

    /**
     * Adds a top-level deployment node to this model.
     *
     * @param name        the name of the deployment node
     * @param description the description of the deployment node
     * @param technology  the technology associated with the deployment node
     * @return a DeploymentNode instance
     * @throws IllegalArgumentException if the name is not specified, or a top-level deployment node with the same name already exists in the model
     */
    @Nonnull
    public DeploymentNode addDeploymentNode(@Nonnull String name, @Nullable String description, @Nullable String technology) {
        return addDeploymentNode(DeploymentElement.DEFAULT_DEPLOYMENT_ENVIRONMENT, name, description, technology);
    }

    /**
     * Adds a top-level deployment node to this model.
     *
     * @param environment   the name of the deployment environment
     * @param name          the name of the deployment node
     * @param description   the description of the deployment node
     * @param technology    the technology associated with the deployment node
     * @return a DeploymentNode instance
     * @throws IllegalArgumentException if the name is not specified, or a top-level deployment node with the same name already exists in the model
     */
    @Nonnull
    public DeploymentNode addDeploymentNode(@Nullable String environment, @Nonnull String name, @Nullable String description, @Nullable String technology) {
        return addDeploymentNode(environment, name, description, technology, 1);
    }

    /**
     * Adds a top-level deployment node to this model.
     *
     * @param name        the name of the deployment node
     * @param description the description of the deployment node
     * @param technology  the technology associated with the deployment node
     * @param instances   the number of instances of the deployment node
     * @return a DeploymentNode instance
     * @throws IllegalArgumentException if the name is not specified, or a top-level deployment node with the same name already exists in the model
     */
    @Nonnull
    public DeploymentNode addDeploymentNode(@Nonnull String name, @Nullable String description, @Nullable String technology, int instances) {
        return addDeploymentNode(DeploymentElement.DEFAULT_DEPLOYMENT_ENVIRONMENT, name, description, technology, instances);
    }

    /**
     * Adds a top-level deployment node to this model.
     *
     * @param environment   the name of the deployment environment
     * @param name          the name of the deployment node
     * @param description   the description of the deployment node
     * @param technology    the technology associated with the deployment node
     * @param instances     the number of instances of the deployment node
     * @return a DeploymentNode instance
     * @throws IllegalArgumentException if the name is not specified, or a top-level deployment node with the same name already exists in the model
     */
    @Nonnull
    public DeploymentNode addDeploymentNode(@Nullable String environment, @Nonnull String name, @Nullable String description, @Nullable String technology, int instances) {
        return addDeploymentNode(environment, name, description, technology, instances, null);
    }

    /**
     * Adds a top-level deployment node to this model.
     *
     * @param name        the name of the deployment node
     * @param description the description of the deployment node
     * @param technology  the technology associated with the deployment node
     * @param instances   the number of instances of the deployment node
     * @param properties  a map of name/value properties associated with the deployment node
     * @return a DeploymentNode instance
     * @throws IllegalArgumentException if the name is not specified, or a top-level deployment node with the same name already exists in the model
     */
    @Nonnull
    public DeploymentNode addDeploymentNode(@Nonnull String name, String description, String technology, int instances, Map<String, String> properties) {
        return addDeploymentNode(DeploymentElement.DEFAULT_DEPLOYMENT_ENVIRONMENT, name, description, technology, instances, properties);
    }

    /**
     * Adds a top-level deployment node to this model.
     *
     * @param environment   the name of the deployment environment
     * @param name          the name of the deployment node
     * @param description   the description of the deployment node
     * @param technology    the technology associated with the deployment node
     * @param instances     the number of instances of the deployment node
     * @param properties    a map of name/value properties associated with the deployment node
     * @return a DeploymentNode instance
     * @throws IllegalArgumentException if the name is not specified, or a top-level deployment node with the same name already exists in the model
     */
    @Nonnull
    public DeploymentNode addDeploymentNode(@Nullable String environment, @Nonnull String name, String description, String technology, int instances, Map<String, String> properties) {
        return addDeploymentNode(null, environment, name, description, technology, instances, properties);
    }

    @Nonnull
    DeploymentNode addDeploymentNode(DeploymentNode parent, @Nullable String environment, @Nonnull String name, String description, String technology, int instances, Map<String, String> properties) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        if ((parent == null && getDeploymentNodeWithName(name, environment) == null) || (parent != null && parent.getDeploymentNodeWithName(name) == null && parent.getInfrastructureNodeWithName(name) == null)) {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setName(name);
            deploymentNode.setDescription(description);
            deploymentNode.setTechnology(technology);
            deploymentNode.setParent(parent);
            deploymentNode.setInstances(instances);
            deploymentNode.setEnvironment(environment);
            if (properties != null) {
                deploymentNode.setProperties(properties);
            }

            if (parent == null) {
                deploymentNodes.add(deploymentNode);
            }

            deploymentNode.setId(idGenerator.generateId(deploymentNode));
            addElementToInternalStructures(deploymentNode);

            return deploymentNode;
        } else {
            throw new IllegalArgumentException("A deployment/infrastructure node named '" + name + "' already exists.");
        }
    }

    @Nonnull
    InfrastructureNode addInfrastructureNode(DeploymentNode parent, @Nonnull String name, String description, String technology, Map<String, String> properties) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        if (parent.getDeploymentNodeWithName(name) == null && parent.getInfrastructureNodeWithName(name) == null) {
            InfrastructureNode infrastructureNode = new InfrastructureNode();
            infrastructureNode.setName(name);
            infrastructureNode.setDescription(description);
            infrastructureNode.setTechnology(technology);
            infrastructureNode.setParent(parent);
            infrastructureNode.setEnvironment(parent.getEnvironment());
            if (properties != null) {
                infrastructureNode.setProperties(properties);
            }

            infrastructureNode.setId(idGenerator.generateId(infrastructureNode));
            addElementToInternalStructures(infrastructureNode);

            return infrastructureNode;
        } else {
            throw new IllegalArgumentException("A deployment/infrastructure node named '" + name + "' already exists.");
        }
    }

    /**
     * Gets the deployment node with the specified name and default environment.
     *
     * @param name      the name of the deployment node
     * @return the DeploymentNode instance with the specified name (or null if it doesn't exist).
     */
    public DeploymentNode getDeploymentNodeWithName(String name) {
        return getDeploymentNodeWithName(name, DeploymentElement.DEFAULT_DEPLOYMENT_ENVIRONMENT);
    }

    /**
     * Gets the deployment node with the specified name and environment.
     *
     * @param name          the name of the deployment node
     * @param environment   the name of the deployment environment
     * @return the DeploymentNode instance with the specified name (or null if it doesn't exist).
     */
    public DeploymentNode getDeploymentNodeWithName(String name, String environment) {
        for (DeploymentNode deploymentNode : getDeploymentNodes()) {
            if (deploymentNode.getEnvironment().equals(environment) && deploymentNode.getName().equals(name)) {
                return deploymentNode;
            }
        }

        return null;
    }

    SoftwareSystemInstance addSoftwareSystemInstance(DeploymentNode deploymentNode, SoftwareSystem softwareSystem, String... deploymentGroups) {
        if (softwareSystem == null) {
            throw new IllegalArgumentException("A software system must be specified.");
        }

        long instanceNumber = deploymentNode.getSoftwareSystemInstances().stream().filter(ssi -> ssi.getSoftwareSystem().equals(softwareSystem)).count();
        instanceNumber++;
        SoftwareSystemInstance softwareSystemInstance = new SoftwareSystemInstance(softwareSystem, (int)instanceNumber, deploymentNode.getEnvironment(), deploymentGroups);
        softwareSystemInstance.setParent(deploymentNode);
        softwareSystemInstance.setId(idGenerator.generateId(softwareSystemInstance));

        replicateElementRelationships(softwareSystemInstance);

        addElementToInternalStructures(softwareSystemInstance);

        return softwareSystemInstance;
    }

    ContainerInstance addContainerInstance(DeploymentNode deploymentNode, Container container, String... deploymentGroups) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }

        long instanceNumber = deploymentNode.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(container)).count();
        instanceNumber++;
        ContainerInstance containerInstance = new ContainerInstance(container, (int)instanceNumber, deploymentNode.getEnvironment(), deploymentGroups);
        containerInstance.setParent(deploymentNode);
        containerInstance.setId(idGenerator.generateId(containerInstance));

        replicateElementRelationships(containerInstance);

        addElementToInternalStructures(containerInstance);

        return containerInstance;
    }

    private void replicateElementRelationships(StaticStructureElementInstance elementInstance) {
        StaticStructureElement element = elementInstance.getElement();

        // find all StaticStructureElementInstance objects in the same deployment environment and deployment group
        Set<StaticStructureElementInstance> elementInstances = getElements().stream()
                .filter(e -> e instanceof StaticStructureElementInstance)
                .map(e -> (StaticStructureElementInstance)e)
                .filter(ssei -> ssei.getEnvironment().equals(elementInstance.getEnvironment()))
                .filter(ssei -> ssei.inSameDeploymentGroup(elementInstance))
                .collect(Collectors.toSet());

        // and replicate the relationships to/from the element instance
        for (StaticStructureElementInstance ssei : elementInstances) {
            StaticStructureElement sse = ssei.getElement();

            for (Relationship relationship : element.getRelationships()) {
                if (relationship.getDestination().equals(sse)) {
                    Relationship newRelationship = addRelationship(elementInstance, ssei, relationship.getDescription(), relationship.getTechnology(), relationship.getInteractionStyle());
                    if (newRelationship != null) {
                        newRelationship.setTags(null);
                        newRelationship.setLinkedRelationshipId(relationship.getId());
                    }
                }
            }

            for (Relationship relationship : sse.getRelationships()) {
                if (relationship.getDestination().equals(element)) {
                    Relationship newRelationship = addRelationship(ssei, elementInstance, relationship.getDescription(), relationship.getTechnology(), relationship.getInteractionStyle());
                    if (newRelationship != null) {
                        newRelationship.setTags(null);
                        newRelationship.setLinkedRelationshipId(relationship.getId());
                    }
                }
            }
        }
    }

    /**
     * Gets the element with the specified canonical name.
     *
     * @param canonicalName the canonical name (e.g. /SoftwareSystem/Container)
     * @return the Element with the given canonical name, or null if one doesn't exist
     * @throws IllegalArgumentException if the canonical name is null or empty
     */
    public Element getElementWithCanonicalName(String canonicalName) {
        if (canonicalName == null || canonicalName.trim().length() == 0) {
            throw new IllegalArgumentException("A canonical name must be specified.");
        }

        for (Element element : getElements()) {
            if (element.getCanonicalName().equals(canonicalName)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Sets the ID generator associated with this model.
     *
     * @param idGenerator an IdGenerate instance
     * @throws IllegalArgumentException if the ID generator is null
     */
    public void setIdGenerator(IdGenerator idGenerator) {
        if (idGenerator == null) {
            throw new IllegalArgumentException("An ID generator must be provided.");
        }

        this.idGenerator = idGenerator;
    }

    /**
     * Provides a way for the description and technology to be modified on an existing relationship.
     *
     * @param relationship  a Relationship instance
     * @param description   the new description
     * @param technology    the new technology
     */
    public void modifyRelationship(Relationship relationship, String description, String technology) {
        if (relationship == null) {
            throw new IllegalArgumentException("A relationship must be specified.");
        }

        if (!relationship.getSource().hasEfferentRelationshipWith(relationship.getDestination(), description)) {
            relationship.setDescription(description);
            relationship.setTechnology(technology);
        } else {
            throw new IllegalArgumentException(
                    String.format("A relationship named \"%s\" between \"%s\" and \"%s\" already exists.",
                            description,
                            relationship.getSource().getName(),
                            relationship.getDestination().getName()));
        }
    }

    /**
     * Gets the strategy in use for creating implied relationships.
     *
     * @return  an ImpliedRelationshipStrategy implementation
     */
    @JsonIgnore
    public ImpliedRelationshipsStrategy getImpliedRelationshipsStrategy() {
        return impliedRelationshipsStrategy;
    }

    /**
     * Sets the strategy is use for creating implied relationships.
     *
     * @param impliedRelationshipStrategy   an ImpliedRelationshipStrategy implementation
     */
    public void setImpliedRelationshipsStrategy(ImpliedRelationshipsStrategy impliedRelationshipStrategy) {
        if (impliedRelationshipStrategy != null) {
            this.impliedRelationshipsStrategy = impliedRelationshipStrategy;
        } else {
            this.impliedRelationshipsStrategy = new DefaultImpliedRelationshipsStrategy();
        }
    }

    /**
     * Gets the collection of name-value property pairs, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    /**
     * Adds a name-value pair property.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

    void setProperties(Map<String, String> properties) {
        if (properties != null) {
            this.properties = new HashMap<>(properties);
        }
    }

}