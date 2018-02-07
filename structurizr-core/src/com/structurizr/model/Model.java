package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A software architecture model.
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

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    /**
     * Creates a software system (location is unspecified) and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param name        the name of the software system
     * @param description a short description of the software system
     * @return the SoftwareSystem instance created and added to the model (or null)
     */
    public SoftwareSystem addSoftwareSystem(String name, String description) {
        return addSoftwareSystem(Location.Unspecified, name, description);
    }

    /**
     * Creates a software system and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param location    the location of the software system (e.g. internal, external, etc)
     * @param name        the name of the software system
     * @param description a short description of the software system
     * @return the SoftwareSystem instance created and added to the model (or null)
     */
    public SoftwareSystem addSoftwareSystem(Location location, String name, String description) {
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
     * Creates a person (location is unspecified) and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description a short description of the person
     * @return the Person instance created and added to the model (or null)
     */
    public Person addPerson(String name, String description) {
        return addPerson(Location.Unspecified, name, description);
    }

    /**
     * Creates a person and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param location    the location of the person (e.g. internal, external, etc)
     * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description a short description of the person
     * @return the Person instance created and added to the model (or null)
     */
    public Person addPerson(Location location, String name, String description) {
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

    Container addContainer(SoftwareSystem parent, String name, String description, String technology) {
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

    Component addComponentAndCode(Container parent, Class<?> type, String description, String technology) {
        return addComponentAndCode(parent, new CodeElement(type), description, technology);
    }

    Component addComponentAndCode(Container parent, String name, String type, String namespace, String description, String technology) {
        return addComponentAndCode(parent, new CodeElement(name, type, namespace), description, technology);
    }

    Component addComponentAndCode(Container parent, CodeElement code, String description, String technology) {
        Component component = new Component();
        component.setName(code.getName());
        component.setType(code);
        component.setDescription(description);
        component.setTechnology(technology);

        component.setParent(parent);
        parent.add(component);

        component.setId(idGenerator.generateId(component));
        addElementToInternalStructures(component);

        return component;
    }

    Component addComponent(Container parent, String name, String description) {
        Component component = new Component();
        component.setName(name);
        component.setDescription(description);

        component.setParent(parent);
        parent.add(component);

        component.setId(idGenerator.generateId(component));
        addElementToInternalStructures(component);

        return component;
    }

    Relationship addRelationship(Element source, Element destination, String description) {
        return addRelationship(source, destination, description, null);
    }

    Relationship addRelationship(Element source, Element destination, String description, String technology) {
        return addRelationship(source, destination, description, technology, InteractionStyle.Synchronous);
    }

    Relationship addRelationship(Element source, Element destination, String description, String technology, InteractionStyle interactionStyle) {
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
     * @return a set containing all elements in this model.
     */
    @JsonIgnore
    public Set<Element> getElements() {
        return new HashSet<>(this.elementsById.values());
    }

    /**
     * @param id the {@link Element#getId()} of the element
     * @return the element in this model with the specified ID (or null if it doesn't exist).
     * @see Element#getId()
     */
    public Element getElement(String id) {
        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("An ID must be specified.");
        }


        return elementsById.get(id);
    }

    /**
     * @return a set containing all relationships in this model.
     */
    @JsonIgnore
    public Set<Relationship> getRelationships() {
        return new HashSet<>(this.relationshipsById.values());
    }

    /**
     * @param id the {@link Relationship#getId()} of the relationship
     * @return the relationship in this model with the specified ID (or null if it doesn't exist).
     * @see Relationship#getId()
     */
    public Relationship getRelationship(String id) {
        return relationshipsById.get(id);
    }

    /**
     * @return a collection containing all of the Person instances in this model.
     */
    public Collection<Person> getPeople() {
        return new LinkedHashSet<>(people);
    }

    /**
     * @return a collection containing all of the SoftwareSystem instances in this model.
     */
    public Set<SoftwareSystem> getSoftwareSystems() {
        return new LinkedHashSet<>(softwareSystems);
    }

    /**
     * @return a collection containing all of the DeploymentNode instances in this model.
     */
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
            containerInstance.setContainer((Container)getElement(containerInstance.getContainerId()));
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
     * @param name the name of a {@link SoftwareSystem}
     * @return the SoftwareSystem instance with the specified name (or null if it doesn't exist).
     */
    public SoftwareSystem getSoftwareSystemWithName(String name) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getName().equals(name)) {
                return softwareSystem;
            }
        }

        return null;
    }

    /**
     * @param id the {@link SoftwareSystem#getId()} of the softwaresystem
     * @return Gets the SoftwareSystem instance with the specified ID (or null if it doesn't exist).
     * @see SoftwareSystem#getId()
     */
    public SoftwareSystem getSoftwareSystemWithId(String id) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getId().equals(id)) {
                return softwareSystem;
            }
        }

        return null;
    }

    /**
     * @param name the name of the person
     * @return the Person instance with the specified name (or null if it doesn't exist).
     */
    public Person getPersonWithName(String name) {
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
     * @return a set of all implicit relationships
     */
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

                Relationship implicitRelationship = addRelationship(source, destination, description, technology);
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

    @JsonIgnore
    public boolean isEmpty() {
        return people.isEmpty() && softwareSystems.isEmpty();
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology) {
        return addDeploymentNode(name, description, technology, 1);
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances) {
        return addDeploymentNode(name, description, technology, instances, null);
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances, Map<String, String> properties) {
        return addDeploymentNode(null, name, description, technology, instances, properties);
    }

    DeploymentNode addDeploymentNode(DeploymentNode parent, String name, String description, String technology, int instances, Map<String, String> properties) {
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

    ContainerInstance addContainerInstance(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }

        long instanceNumber = getElements().stream().filter(e -> e instanceof ContainerInstance && ((ContainerInstance)e).getContainer().equals(container)).count();
        instanceNumber++;
        ContainerInstance containerInstance = new ContainerInstance(container, (int)instanceNumber);
        containerInstance.setId(idGenerator.generateId(containerInstance));

        // find all ContainerInstance objects
        Set<ContainerInstance> containerInstances = getElements().stream()
                .filter(e -> e instanceof ContainerInstance)
                .map(e -> (ContainerInstance)e)
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

        addElementToInternalStructures(containerInstance);

        return containerInstance;
    }

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

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}