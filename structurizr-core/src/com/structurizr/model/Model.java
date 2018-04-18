package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public Model() {
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
        if (destination == null) {
            throw new IllegalArgumentException("The destination must be specified.");

        }

        Relationship relationship = new Relationship(source, destination, description, technology, interactionStyle);
        if (addRelationship(relationship)) {
            return relationship;
        } else {
            return null;
        }
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
        elementsById.put(element.getId(), element);
        element.setModel(this);
        idGenerator.found(element.getId());
    }

    private void addRelationshipToInternalStructures(Relationship relationship) {
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
            throw new IllegalArgumentException("An relationship ID must be specified.");
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

    /**
     * Gets the set of all software systems in this model.
     *
     * @return a Set of SoftwareSystem instances
     */
    @Nonnull
    public Set<SoftwareSystem> getSoftwareSystems() {
        return new LinkedHashSet<>(softwareSystems);
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

    public void hydrate() {
        // add all of the elements to the model
        people.forEach(this::addElementToInternalStructures);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            addElementToInternalStructures(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                softwareSystem.add(container);
                addElementToInternalStructures(container);
                container.setParent(softwareSystem);
                for (Component component : container.getComponents()) {
                    container.add(component);
                    addElementToInternalStructures(component);
                    component.setParent(container);
                }
            }
        }

        deploymentNodes.forEach(dn -> hydrateDeploymentNode(dn, null));

        // now hydrate the relationships
        people.forEach(this::hydrateRelationships);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            hydrateRelationships(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                hydrateRelationships(container);
                for (Component component : container.getComponents()) {
                    hydrateRelationships(component);
                }
            }
        }

        deploymentNodes.forEach(this::hydrateDeploymentNodeRelationships);
    }

    private void hydrateDeploymentNode(DeploymentNode deploymentNode, DeploymentNode parent) {
        deploymentNode.setParent(parent);
        addElementToInternalStructures(deploymentNode);

        deploymentNode.getChildren().forEach(child -> hydrateDeploymentNode(child, deploymentNode));

        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            containerInstance.setContainer((Container) getElement(containerInstance.getContainerId()));
            addElementToInternalStructures(containerInstance);
        }
    }

    private void hydrateDeploymentNodeRelationships(DeploymentNode deploymentNode) {
        hydrateRelationships(deploymentNode);
        deploymentNode.getChildren().forEach(this::hydrateDeploymentNodeRelationships);
        deploymentNode.getContainerInstances().forEach(this::hydrateRelationships);
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
     */
    @Nonnull
    public Set<Relationship> addImplicitRelationships() {
        Set<Relationship> implicitRelationships = new HashSet<>();

        String descriptionKey = "D";
        String technologyKey = "T";
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
                            }

                            if (relationship.getDescription() != null) {
                                candidateRelationships.get(source).get(destination).get(descriptionKey).add(relationship.getDescription());
                            }

                            if (relationship.getTechnology() != null) {
                                candidateRelationships.get(source).get(destination).get(technologyKey).add(relationship.getTechnology());
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

                String description = "";
                if (possibleDescriptions.size() == 1) {
                    description = possibleDescriptions.iterator().next();
                }

                String technology = "";
                if (possibleTechnologies.size() == 1) {
                    technology = possibleTechnologies.iterator().next();
                }

                // todo ... this defaults to being a synchronous relationship
                Relationship implicitRelationship = addRelationship(source, destination, description, technology, InteractionStyle.Synchronous);
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

        if (source.getParent() != null) {
            if (destination.equals(source.getParent())) {
                return false;
            }

            if (source.getParent().getParent() != null) {
                if (destination.equals(source.getParent().getParent())) {
                    return false;
                }
            }
        }

        if (destination.getParent() != null) {
            if (source.equals(destination.getParent())) {
                return false;
            }

            if (destination.getParent().getParent() != null) {
                if (source.equals(destination.getParent().getParent())) {
                    return false;
                }
            }
        }

        return true;
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
        return addDeploymentNode(name, description, technology, 1);
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
        return addDeploymentNode(name, description, technology, instances, null);
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
        return addDeploymentNode(null, name, description, technology, instances, properties);
    }

    @Nonnull
    DeploymentNode addDeploymentNode(DeploymentNode parent, @Nonnull String name, String description, String technology, int instances, Map<String, String> properties) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        if ((parent == null && getDeploymentNodeWithName(name) == null) || (parent != null && parent.getDeploymentNodeWithName(name) == null)) {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setName(name);
            deploymentNode.setDescription(description);
            deploymentNode.setTechnology(technology);
            deploymentNode.setParent(parent);
            deploymentNode.setInstances(instances);
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
            throw new IllegalArgumentException("A deployment node named '" + name + "' already exists.");
        }
    }

    /**
     * @param name the name of the deployment node
     * @return the DeploymentNode instance with the specified name (or null if it doesn't exist).
     */
    public DeploymentNode getDeploymentNodeWithName(String name) {
        for (DeploymentNode deploymentNode : getDeploymentNodes()) {
            if (deploymentNode.getName().equals(name)) {
                return deploymentNode;
            }
        }

        return null;
    }

    ContainerInstance addContainerInstance(Container container, boolean replicateContainerRelationships) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }

        long instanceNumber = getElements().stream().filter(e -> e instanceof ContainerInstance && ((ContainerInstance) e).getContainer().equals(container)).count();
        instanceNumber++;
        ContainerInstance containerInstance = new ContainerInstance(container, (int) instanceNumber);
        containerInstance.setId(idGenerator.generateId(containerInstance));

        if (replicateContainerRelationships) {
            // find all ContainerInstance objects
            Set<ContainerInstance> containerInstances = getElements().stream()
                    .filter(e -> e instanceof ContainerInstance)
                    .map(e -> (ContainerInstance) e)
                    .collect(Collectors.toSet());

            // and replicate the container-container relationships
            for (ContainerInstance ci : containerInstances) {
                Container c = ci.getContainer();

                for (Relationship relationship : container.getRelationships()) {
                    if (relationship.getDestination().equals(c)) {
                        addRelationship(containerInstance, ci, relationship.getDescription(), relationship.getTechnology(), relationship.getInteractionStyle());
                    }
                }

                for (Relationship relationship : c.getRelationships()) {
                    if (relationship.getDestination().equals(container)) {
                        addRelationship(ci, containerInstance, relationship.getDescription(), relationship.getTechnology(), relationship.getInteractionStyle());
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

}