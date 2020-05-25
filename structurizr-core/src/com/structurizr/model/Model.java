package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.WorkspaceValidationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a software architecture model, into which all model elements are added.
 */
public final class Model {

    private IdGenerator idGenerator = new SequentialIntegerIdGeneratorStrategy();

    private final Map<String, Element> elementsById = new HashMap<>();
    private final Map<String, Relationship> relationshipsById = new HashMap<>();

    private Enterprise enterprise;

    private Set<Person> people = new LinkedHashSet<>();
    private Set<SoftwareSystem> softwareSystems = new LinkedHashSet<>();
    private Set<DeploymentNode> deploymentNodes = new LinkedHashSet<>();

    private ImpliedRelationshipsStrategy impliedRelationshipsStrategy = new DefaultImpliedRelationshipsStrategy();

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
     *
     * @param enterprise an Enterprise instance
     */
    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    /**
     * Creates a software system (with an unspecified location) and adds it to the model.
     *
     * @param name        the name of the software system
     * @param description a short description of the software system
     * @return the SoftwareSystem instance created and added to the model (or null)
     * @throws IllegalArgumentException if a software system with the same name already exists
     */
    public SoftwareSystem addSoftwareSystem(@Nonnull String name, @Nullable String description) {
        return addSoftwareSystem(Location.Unspecified, name, description);
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
            throw new IllegalArgumentException("A software system named '" + name + "' already exists.");
        }
    }

    /**
     * Creates a person (with an unspecified location) and adds it to the model.
     *
     * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description a short description of the person
     * @return the Person instance created and added to the model (or null)
     * @throws IllegalArgumentException if a person with the same name already exists
     */
    @Nonnull
    public Person addPerson(@Nonnull String name, @Nullable String description) {
        return addPerson(Location.Unspecified, name, description);
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
            throw new IllegalArgumentException("A person named '" + name + "' already exists.");
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

    Component addComponentOfType(Container parent, String name, String type, String description, String technology) {
        if (parent.getComponentWithName(name) == null) {
            Component component = new Component();
            component.setName(name);
            component.setDescription(description);
            component.setTechnology(technology);

            if (type != null && type.trim().length() > 0) {
                component.setType(type);
            }

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
    Relationship addRelationship(Element source, @Nonnull Element destination, String description, String technology, InteractionStyle interactionStyle) {
        return addRelationship(source, destination, description, technology, interactionStyle, true);
    }

    @Nullable
    Relationship addRelationship(Element source, @Nonnull Element destination, String description, String technology, InteractionStyle interactionStyle, boolean createImpliedRelationships) {
        if (destination == null) {
            throw new IllegalArgumentException("The destination must be specified.");
        }

        if (isChildOf(source, destination) || isChildOf(destination, source)) {
            throw new IllegalArgumentException("Relationships cannot be added between parents and children.");
        }

        Relationship relationship = new Relationship(source, destination, description, technology, interactionStyle);
        if (addRelationship(relationship)) {

            if (createImpliedRelationships) {
                if
                (
                    (source instanceof Person || source instanceof SoftwareSystem || source instanceof Container || source instanceof Component) &&
                    (destination instanceof Person || destination instanceof SoftwareSystem || destination instanceof Container || destination instanceof Component)
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
        deploymentNode.setParent(parent);
        addElementToInternalStructures(deploymentNode);

        deploymentNode.getChildren().forEach(child -> hydrateDeploymentNode(child, deploymentNode));

        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            containerInstance.setContainer((Container)getElement(containerInstance.getContainerId()));
            addElementToInternalStructures(containerInstance);
        }

        for (InfrastructureNode infrastructureNode : deploymentNode.getInfrastructureNodes()) {
            infrastructureNode.setParent(deploymentNode);
            addElementToInternalStructures(infrastructureNode);
        }
    }

    private void checkNameIsUnique(Collection<? extends Element> elements, String name, String errorMessage) {
        if (elements.stream().filter(e -> e.getName().equalsIgnoreCase(name)).count() != 1) {
            throw new WorkspaceValidationException(
                    String.format(errorMessage, name));
        }
    }

    private void checkNameIsUnique(Collection<DeploymentNode> deploymentNodes, String name, String environment, String errorMessage) {
        if (deploymentNodes.stream().filter(dn -> dn.getName().equalsIgnoreCase(name) && dn.getEnvironment().equals(environment)).count() != 1) {
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
        if (relationships.stream().filter(r -> r.getDestination().equals(relationship.getDestination()) && r.getDescription().equalsIgnoreCase(relationship.getDescription())).count() != 1) {
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
     * <p>Propagates all relationships from children to their parents. For example, if you have two components (AAA and BBB)
     * in different software systems that have a relationship, calling this method will add the following
     * additional implied relationships to the model: AAA-&gt;BB AAA--&gt;B AA-&gt;BBB AA-&gt;BB AA-&gt;B A-&gt;BBB A-&gt;BB A-&gt;B.</p>
     *
     * @return a set of all implicit relationships that were added to the model
     * @deprecated use {@link #setImpliedRelationshipsStrategy(ImpliedRelationshipsStrategy)} ()} instead to set a strategy, before creating relationships
     */
    @Nonnull
    @Deprecated
    public Set<Relationship> addImplicitRelationships() {
        Set<Relationship> implicitRelationships = new HashSet<>();

        String descriptionKey = "D";
        String technologyKey = "T";
        String interactionStyleKey = "I";
        Map<Element, Map<Element, Map<String, HashSet<String>>>> candidateRelationships = new HashMap<>();

        for (Relationship relationship : getRelationships()) {
            Element source = relationship.getSource();
            Element destination = relationship.getDestination();

            while (source != null) {
                while (destination != null) {
                    if (!source.hasEfferentRelationshipWith(destination)) {
                        if (propagatedRelationshipIsAllowed(source, destination)) {

                            if (!candidateRelationships.containsKey(source)) {
                                candidateRelationships.put(source, new HashMap<>());
                            }

                            if (!candidateRelationships.get(source).containsKey(destination)) {
                                candidateRelationships.get(source).put(destination, new HashMap<>());
                                candidateRelationships.get(source).get(destination).put(descriptionKey, new HashSet<>());
                                candidateRelationships.get(source).get(destination).put(technologyKey, new HashSet<>());
                                candidateRelationships.get(source).get(destination).put(interactionStyleKey, new HashSet<>());
                            }

                            if (relationship.getDescription() != null) {
                                candidateRelationships.get(source).get(destination).get(descriptionKey).add(relationship.getDescription());
                            }

                            if (relationship.getTechnology() != null) {
                                candidateRelationships.get(source).get(destination).get(technologyKey).add(relationship.getTechnology());
                            }

                            if (relationship.getInteractionStyle() != null) {
                                candidateRelationships.get(source).get(destination).get(interactionStyleKey).add(relationship.getInteractionStyle().toString());
                            }
                        }
                    }

                    destination = destination.getParent();
                }

                destination = relationship.getDestination();
                source = source.getParent();
            }
        }

        for (Element source : candidateRelationships.keySet()) {
            for (Element destination : candidateRelationships.get(source).keySet()) {
                Set<String> possibleDescriptions = candidateRelationships.get(source).get(destination).get(descriptionKey);
                Set<String> possibleTechnologies = candidateRelationships.get(source).get(destination).get(technologyKey);
                Set<String> possibleInteractionStyles = candidateRelationships.get(source).get(destination).get(interactionStyleKey);

                String description = "";
                if (possibleDescriptions.size() == 1) {
                    description = possibleDescriptions.iterator().next();
                }

                String technology = "";
                if (possibleTechnologies.size() == 1) {
                    technology = possibleTechnologies.iterator().next();
                }

                InteractionStyle interactionStyle = InteractionStyle.Synchronous;
                // If any async relationships are present, mark the relationship as asynchronous
                if (possibleInteractionStyles.contains(InteractionStyle.Asynchronous.toString())) {
                    interactionStyle = InteractionStyle.Asynchronous;
                }

                Relationship implicitRelationship = addRelationship(source, destination, description, technology, interactionStyle);
                if (implicitRelationship != null) {
                    implicitRelationships.add(implicitRelationship);
                }
            }
        }

        return implicitRelationships;
    }

    private boolean propagatedRelationshipIsAllowed(Element source, Element destination) {
        if (source.equals(destination)) {
            return false;
        }

        return !(isChildOf(source, destination) || isChildOf(destination, source));
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

    ContainerInstance addContainerInstance(DeploymentNode deploymentNode, Container container, boolean replicateContainerRelationships) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }

        long instanceNumber = getElements().stream().filter(e -> e instanceof ContainerInstance && ((ContainerInstance) e).getContainer().equals(container)).count();
        instanceNumber++;
        ContainerInstance containerInstance = new ContainerInstance(container, (int) instanceNumber, deploymentNode.getEnvironment());
        containerInstance.setId(idGenerator.generateId(containerInstance));

        if (replicateContainerRelationships) {
            // find all ContainerInstance objects in the same deployment environment
            Set<ContainerInstance> containerInstances = getElements().stream()
                    .filter(e -> e instanceof ContainerInstance && ((ContainerInstance) e).getEnvironment().equals(deploymentNode.getEnvironment()))
                    .map(e -> (ContainerInstance) e)
                    .collect(Collectors.toSet());

            // and replicate the container-container relationships within the same deployment environment
            for (ContainerInstance ci : containerInstances) {
                Container c = ci.getContainer();

                for (Relationship relationship : container.getRelationships()) {
                    if (relationship.getDestination().equals(c)) {
                        Relationship newRelationship = addRelationship(containerInstance, ci, relationship.getDescription(), relationship.getTechnology(), relationship.getInteractionStyle());
                        if (newRelationship != null) {
                            newRelationship.setTags(null);
                            newRelationship.setLinkedRelationshipId(relationship.getId());
                        }
                    }
                }

                for (Relationship relationship : c.getRelationships()) {
                    if (relationship.getDestination().equals(container)) {
                        Relationship newRelationship = addRelationship(ci, containerInstance, relationship.getDescription(), relationship.getTechnology(), relationship.getInteractionStyle());
                        if (newRelationship != null) {
                            newRelationship.setTags(null);
                            newRelationship.setLinkedRelationshipId(relationship.getId());
                        }
                    }
                }
            }
        }

        addElementToInternalStructures(containerInstance);

        return containerInstance;
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

        // canonical names start with a leading slash, so add this if it's missing
        if (!canonicalName.startsWith("/")) {
            canonicalName = "/" + canonicalName;
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

        Relationship newRelationship = new Relationship(relationship.getSource(), relationship.getDestination(), description, technology, relationship.getInteractionStyle());
        if (!relationship.getSource().has(newRelationship)) {
            relationship.setDescription(description);
            relationship.setTechnology(technology);
        } else {
            throw new IllegalArgumentException("This relationship exists already: " + newRelationship);
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

}