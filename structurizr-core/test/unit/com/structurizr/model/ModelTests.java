package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTests extends AbstractWorkspaceTestBase {

    @Test
    void test_addSoftwareSystem_ThrowsAnException_WhenANullNameIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            model.addSoftwareSystem(null, "");
        });
    }

    @Test
    void test_addSoftwareSystem_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            model.addSoftwareSystem(" ", "");
        });
    }

    @Test
    void test_addPerson_ThrowsAnException_WhenANullNameIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            model.addPerson(null, "");
        });
    }

    @Test
    void test_addPerson_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            model.addPerson(" ", "");
        });
    }

    @Test
    void test_addSoftwareSystem_AddsTheSoftwareSystem_WhenASoftwareSystemDoesNotExistWithTheSameName() {
        assertTrue(model.getSoftwareSystems().isEmpty());
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Some description");
        assertEquals(1, model.getSoftwareSystems().size());

        assertEquals(Location.External, softwareSystem.getLocation());
        assertEquals("System A", softwareSystem.getName());
        assertEquals("Some description", softwareSystem.getDescription());
        assertEquals("1", softwareSystem.getId());
        assertSame(softwareSystem, model.getSoftwareSystems().iterator().next());
    }

    @Test
    void test_addSoftwareSystem_ThrowsAnException_WhenASoftwareSystemExistsWithTheSameName() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Some description");
        assertEquals(1, model.getSoftwareSystems().size());

        try {
            model.addSoftwareSystem(Location.External, "System A", "Description");
            fail();
        } catch (Exception e) {
            assertEquals("A top-level element named 'System A' already exists.", e.getMessage());
        }
    }

    @Test
    void test_addSoftwareSystemWithoutSpecifyingLocation_AddsTheSoftwareSystem_WhenASoftwareSystemDoesNotExistWithTheSameName() {
        assertTrue(model.getSoftwareSystems().isEmpty());
        SoftwareSystem softwareSystem = model.addSoftwareSystem("System A", "Some description");
        assertEquals(1, model.getSoftwareSystems().size());

        assertEquals(Location.Unspecified, softwareSystem.getLocation());
        assertEquals("System A", softwareSystem.getName());
        assertEquals("Some description", softwareSystem.getDescription());
        assertEquals("1", softwareSystem.getId());
        assertSame(softwareSystem, model.getSoftwareSystems().iterator().next());
    }

    @Test
    void test_addPerson_AddsThePerson_WhenAPersonDoesNotExistWithTheSameName() {
        assertTrue(model.getPeople().isEmpty());
        Person person = model.addPerson(Location.Internal, "Some internal user", "Some description");
        assertEquals(1, model.getPeople().size());

        assertEquals(Location.Internal, person.getLocation());
        assertEquals("Some internal user", person.getName());
        assertEquals("Some description", person.getDescription());
        assertEquals("1", person.getId());
        assertSame(person, model.getPeople().iterator().next());
    }

    @Test
    void test_addPerson_ThrowsAnException_WhenAPersonExistsWithTheSameName() {
        Person person = model.addPerson(Location.Internal, "Admin User", "Description");
        assertEquals(1, model.getPeople().size());

        try {
            model.addPerson(Location.External, "Admin User", "Description");
            fail();
        } catch (Exception e) {
            assertEquals("A top-level element named 'Admin User' already exists.", e.getMessage());
        }
    }

    @Test
    void test_addPerson_AddsThePersonWithoutSpecifyingTheLocation_WhenAPersonDoesNotExistWithTheSameName() {
        assertTrue(model.getPeople().isEmpty());
        Person person = model.addPerson("Some internal user", "Some description");
        assertEquals(1, model.getPeople().size());

        assertEquals(Location.Unspecified, person.getLocation());
        assertEquals("Some internal user", person.getName());
        assertEquals("Some description", person.getDescription());
        assertEquals("1", person.getId());
        assertSame(person, model.getPeople().iterator().next());
    }

    @Test
    void test_getElement_ReturnsNull_WhenAnElementWithTheSpecifiedIdDoesNotExist() {
        assertNull(model.getElement("100"));
    }

    @Test
    void test_getElement_ReturnsAnElement_WhenAnElementWithTheSpecifiedIdDoesExist() {
        Person person = model.addPerson(Location.Internal, "Name", "Description");
        assertSame(person, model.getElement(person.getId()));
    }

    @Test
    void test_contains_ReturnsFalse_WhenTheSpecifiedElementIsNotInTheModel() {
        Model newModel = new Model();
        SoftwareSystem softwareSystem = newModel.addSoftwareSystem(Location.Unspecified, "Name", "Description");
        assertFalse(model.contains(softwareSystem));
    }

    @Test
    void test_contains_ReturnsTrue_WhenTheSpecifiedElementIsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Unspecified, "Name", "Description");
        assertTrue(model.contains(softwareSystem));
    }

    @Test
    void test_getSoftwareSystemWithName_ReturnsNull_WhenASoftwareSystemWithTheSpecifiedNameDoesNotExist() {
        assertNull(model.getSoftwareSystemWithName("System X"));
    }

    @Test
    void test_getSoftwareSystemWithName_ReturnsASoftwareSystem_WhenASoftwareSystemWithTheSpecifiedNameExists() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Description");
        assertSame(softwareSystem, model.getSoftwareSystemWithName("System A"));
    }

    @Test
    void test_getSoftwareSystemWithId_ThrowsAnException_WhenPassedANullId() {
        try {
            model.getSoftwareSystemWithId(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A software system ID must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_getSoftwareSystemWithId_ThrowsAnException_WhenPassedAnEmptyId() {
        try {
            model.getSoftwareSystemWithId(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A software system ID must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_getSoftwareSystemWithId_ReturnsNull_WhenASoftwareSystemWithTheSpecifiedIdDoesNotExist() {
        assertNull(model.getSoftwareSystemWithId("100"));
    }

    @Test
    void test_getSoftwareSystemWithId_ReturnsASoftwareSystem_WhenASoftwareSystemWithTheSpecifiedIdDoesExist() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Description");
        assertSame(softwareSystem, model.getSoftwareSystemWithId(softwareSystem.getId()));
    }

    @Test
    void test_getPersonWithName_ReturnsNull_WhenAPersonWithTheSpecifiedNameDoesNotExist() {
        assertNull(model.getPersonWithName("Admin User"));
    }

    @Test
    void test_getPersonWithName_ReturnsAPerson_WhenAPersonWithTheSpecifiedNameExists() {
        Person person = model.addPerson(Location.External, "Admin User", "Description");
        assertSame(person, model.getPersonWithName("Admin User"));
    }

    @Test
    void test_getRelationship_ThrowsAnException_WhenPassedANullId() {
        try {
            model.getRelationship(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship ID must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_getRelationship_ThrowsAnException_WhenPassedAnEmptyId() {
        try {
            model.getRelationship(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship ID must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_addRelationship_AddsARelationshipWithTheSpecifiedDescriptionAndTechnologyAndInteractionStyle() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Relationship relationship = model.addRelationship(a, b, "Uses", "HTTPS", InteractionStyle.Asynchronous);

        assertSame(a, relationship.getSource());
        assertSame(b, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertEquals("HTTPS", relationship.getTechnology());
        assertEquals(InteractionStyle.Asynchronous, relationship.getInteractionStyle());

        assertTrue(model.getRelationships().contains(relationship));
    }

    @Test
    void test_addRelationship_DisallowsTheSameRelationshipToBeAddedMoreThanOnce() {
        SoftwareSystem element1 = model.addSoftwareSystem("Element 1", "Description");
        SoftwareSystem element2 = model.addSoftwareSystem("Element 2", "Description");
        Relationship relationship1 = element1.uses(element2, "Uses", "");
        Relationship relationship2 = element1.uses(element2, "Uses", "");
        assertTrue(element1.has(relationship1));
        assertNull(relationship2);
        assertEquals(1, element1.getRelationships().size());
    }

    @Test
    void test_addRelationship_AllowsMultipleRelationshipsBetweenElements() {
        SoftwareSystem element1 = model.addSoftwareSystem("Element 1", "Description");
        SoftwareSystem element2 = model.addSoftwareSystem("Element 2", "Description");
        Relationship relationship1 = element1.uses(element2, "Uses in some way", "");
        Relationship relationship2 = element1.uses(element2, "Uses in another way", "");
        assertTrue(element1.has(relationship1));
        assertTrue(element1.has(relationship2));
        assertEquals(2, element1.getRelationships().size());
    }

    @Test
    void test_addRelationship_ThrowsAnException_WhenTheDestinationIsAChildOfTheSource() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "", "");
        Component component = container.addComponent("Component", "", "");

        try {
            softwareSystem.uses(container, "Uses");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Relationships cannot be added between parents and children.", iae.getMessage());
            assertEquals(0, softwareSystem.getRelationships().size());
        }

        try {
            container.uses(component, "Uses");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Relationships cannot be added between parents and children.", iae.getMessage());
            assertEquals(0, softwareSystem.getRelationships().size());
        }

        try {
            softwareSystem.uses(component, "Uses");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Relationships cannot be added between parents and children.", iae.getMessage());
            assertEquals(0, softwareSystem.getRelationships().size());
        }
    }

    @Test
    void test_addRelationship_ThrowsAnException_WhenTheSourceIsAChildOfTheDestination() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "", "");
        Component component = container.addComponent("Component", "", "");

        try {
            container.uses(softwareSystem, "Uses");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Relationships cannot be added between parents and children.", iae.getMessage());
            assertEquals(0, softwareSystem.getRelationships().size());
        }

        try {
            component.uses(container, "Uses");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Relationships cannot be added between parents and children.", iae.getMessage());
            assertEquals(0, softwareSystem.getRelationships().size());
        }

        try {
            component.uses(softwareSystem, "Uses");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Relationships cannot be added between parents and children.", iae.getMessage());
            assertEquals(0, softwareSystem.getRelationships().size());
        }
    }

    @Test
    void test_modifyRelationship_ThrowsAnException_WhenARelationshipIsNotSpecified() {
        try {
            model.modifyRelationship(null, "Uses", "Technology");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_modifyRelationship_ModifiesAnExistingRelationship_WhenThatRelationshipDoesNotAlreadyExist() {
        SoftwareSystem element1 = model.addSoftwareSystem("Element 1", "Description");
        SoftwareSystem element2 = model.addSoftwareSystem("Element 2", "Description");
        Relationship relationship = element1.uses(element2, "", "");

        model.modifyRelationship(relationship, "Uses", "Technology");
        assertEquals("Uses", relationship.getDescription());
        assertEquals("Technology", relationship.getTechnology());
    }

    @Test
    void test_modifyRelationship_ThrowsAnException_WhenThatRelationshipDoesAlreadyExist() {
        SoftwareSystem element1 = model.addSoftwareSystem("Element 1", "Description");
        SoftwareSystem element2 = model.addSoftwareSystem("Element 2", "Description");
        Relationship relationship = element1.uses(element2, "Uses", "Technology");

        try {
            model.modifyRelationship(relationship, "Uses", "Technology");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship named \"Uses\" between \"Element 1\" and \"Element 2\" already exists.", iae.getMessage());
        }
    }

    @Test
    void test_addSoftwareSystemInstance_ThrowsAnException_WhenANullSoftwareSystemIsSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
            deploymentNode.add((SoftwareSystem) null);
            fail();
        } catch (Exception e) {
            assertEquals("A software system must be specified.", e.getMessage());
        }
    }

    @Test
    void test_addContainerInstance_ThrowsAnException_WhenANullContainerIsSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
            deploymentNode.add((Container) null);
            fail();
        } catch (Exception e) {
            assertEquals("A container must be specified.", e.getMessage());
        }
    }

    @Test
    void test_addDeploymentNode_AddsADeploymentNode_WhenADeploymentEnvironmentIsNotSpecified() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");

        assertEquals("Deployment Node", deploymentNode.getName());
        assertEquals("Description", deploymentNode.getDescription());
        assertEquals("Technology", deploymentNode.getTechnology());
        assertEquals("Default", deploymentNode.getEnvironment());
    }

    @Test
    void test_addDeploymentNode_AddsADeploymentNode_WhenADeploymentEnvironmentIsSpecified() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Development", "Deployment Node", "Description", "Technology");

        assertEquals("Deployment Node", deploymentNode.getName());
        assertEquals("Description", deploymentNode.getDescription());
        assertEquals("Technology", deploymentNode.getTechnology());
        assertEquals("Development", deploymentNode.getEnvironment());
    }

    @Test
    void test_addElementInstance_AddsElementInstancesAndReplicatesRelationshipsWithinTheDeploymentEnvironment() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        Container container1 = softwareSystem1.addContainer("Container 1", "Description", "Technology");

        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        Container container2 = softwareSystem2.addContainer("Container 2", "Description", "Technology");

        SoftwareSystem softwareSystem3 = model.addSoftwareSystem("Software System 3", "Description");
        Container container3 = softwareSystem3.addContainer("Container 3", "Description", "Technology");

        SoftwareSystem softwareSystem4 = model.addSoftwareSystem("Software System 4", "Description");

        container1.uses(container2, "Uses 1", "Technology 1", InteractionStyle.Synchronous);
        container2.uses(container3, "Uses 2", "Technology 2", InteractionStyle.Asynchronous);
        container3.uses(softwareSystem4, "Uses");

        DeploymentNode developmentDeploymentNode = model.addDeploymentNode("Development", "Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance1 = developmentDeploymentNode.add(container1);
        ContainerInstance containerInstance2 = developmentDeploymentNode.add(container2);
        ContainerInstance containerInstance3 = developmentDeploymentNode.add(container3);
        SoftwareSystemInstance softwareSystemInstance = developmentDeploymentNode.add(softwareSystem4);

        // the following live element instances should not affect the relationships of the development element instances
        DeploymentNode liveDeploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        liveDeploymentNode.add(container1);
        liveDeploymentNode.add(container2);
        liveDeploymentNode.add(container3);
        liveDeploymentNode.add(softwareSystem4);

        assertEquals(1, containerInstance1.getRelationships().size());
        Relationship relationship = containerInstance1.getRelationships().iterator().next();
        assertSame(containerInstance1, relationship.getSource());
        assertSame(containerInstance2, relationship.getDestination());
        assertEquals("Uses 1", relationship.getDescription());
        assertEquals("Technology 1", relationship.getTechnology());
        assertEquals(InteractionStyle.Synchronous, relationship.getInteractionStyle());
        assertEquals("", relationship.getTags());

        assertEquals(1, containerInstance2.getRelationships().size());
        relationship = containerInstance2.getRelationships().iterator().next();
        assertSame(containerInstance2, relationship.getSource());
        assertSame(containerInstance3, relationship.getDestination());
        assertEquals("Uses 2", relationship.getDescription());
        assertEquals("Technology 2", relationship.getTechnology());
        assertEquals(InteractionStyle.Asynchronous, relationship.getInteractionStyle());
        assertEquals("", relationship.getTags());

        assertEquals(1, containerInstance3.getRelationships().size());
        relationship = containerInstance3.getRelationships().iterator().next();
        assertSame(containerInstance3, relationship.getSource());
        assertSame(softwareSystemInstance, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertNull(relationship.getTechnology());
        assertNull(relationship.getInteractionStyle());
        assertEquals("", relationship.getTags());
    }

    @Test
    void test_addElementInstance_AddsElementInstancesAndReplicatesRelationshipsWithinTheDeploymentEnvironmentAndDefaultGroup() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System");
        Container api = softwareSystem1.addContainer("API");
        Container database = softwareSystem1.addContainer("Database");
        api.uses(database, "Uses");

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        ContainerInstance apiInstance1 = liveDeploymentNode.add(api);
        ContainerInstance databaseInstance1 = liveDeploymentNode.add(database);

        ContainerInstance apiInstance2 = liveDeploymentNode.add(api);
        ContainerInstance databaseInstance2 = liveDeploymentNode.add(database);

        assertEquals(2, apiInstance1.getRelationships().size());
        assertEquals(2, apiInstance2.getRelationships().size());

        // apiInstance1 -> databaseInstance1
        Relationship relationship = apiInstance1.getEfferentRelationshipWith(databaseInstance1);
        assertEquals("Uses", relationship.getDescription());

        // apiInstance1 -> databaseInstance2
        relationship = apiInstance1.getEfferentRelationshipWith(databaseInstance2);
        assertEquals("Uses", relationship.getDescription());

        // apiInstance2 -> databaseInstance1
        relationship = apiInstance2.getEfferentRelationshipWith(databaseInstance1);
        assertEquals("Uses", relationship.getDescription());

        // apiInstance2 -> databaseInstance2
        relationship = apiInstance2.getEfferentRelationshipWith(databaseInstance2);
        assertEquals("Uses", relationship.getDescription());
    }

    @Test
    void test_addElementInstance_AddsElementInstancesAndReplicatesRelationshipsWithinTheDeploymentEnvironmentAndSpecifiedGroup() {
        // in this test, container instances are added to two deployment groups: "Instance 1" and "Instance 2"
        // relationships are not replicated between element instances in other groups

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container api = softwareSystem.addContainer("API");
        Container database = softwareSystem.addContainer("Database");
        api.uses(database, "Uses");

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        ContainerInstance apiInstance1 = liveDeploymentNode.add(api, "Instance 1");
        ContainerInstance databaseInstance1 = liveDeploymentNode.add(database, "Instance 1");

        ContainerInstance apiInstance2 = liveDeploymentNode.add(api, "Instance 2");
        ContainerInstance databaseInstance2 = liveDeploymentNode.add(database, "Instance 2");

        assertEquals(1, apiInstance1.getRelationships().size());
        assertEquals(1, apiInstance2.getRelationships().size());

        // apiInstance1 -> databaseInstance1
        Relationship relationship = apiInstance1.getEfferentRelationshipWith(databaseInstance1);
        assertEquals("Uses", relationship.getDescription());

        // apiInstance2 -> databaseInstance2
        relationship = apiInstance2.getEfferentRelationshipWith(databaseInstance2);
        assertEquals("Uses", relationship.getDescription());
    }

    @Test
    void test_addElementInstance_AddsElementInstancesAndReplicatesRelationshipsWithinTheDeploymentEnvironmentAndSpecifiedGroups() {
        // in this test:
        // - API container instances are added to "Instance 1", "Instance 2" and "Shared"
        // - database container instances are added to "Instance 1" and "Instance 2"
        // - a shared container is added to "Shared"

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container api = softwareSystem.addContainer("API");
        Container database = softwareSystem.addContainer("Database");
        Container cache = softwareSystem.addContainer("Cache");
        api.uses(database, "Uses");
        api.uses(cache, "Uses");

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("Live");
        ContainerInstance apiInstance1 = liveDeploymentNode.add(api, "Instance 1", "Shared");
        ContainerInstance databaseInstance1 = liveDeploymentNode.add(database, "Instance 1");

        ContainerInstance apiInstance2 = liveDeploymentNode.add(api, "Instance 2", "Shared");
        ContainerInstance databaseInstance2 = liveDeploymentNode.add(database, "Instance 2");

        ContainerInstance cacheInstance = liveDeploymentNode.add(cache, "Shared");

        assertEquals(2, apiInstance1.getRelationships().size());
        assertTrue(apiInstance1.hasEfferentRelationshipWith(databaseInstance1));
        assertTrue(apiInstance1.hasEfferentRelationshipWith(cacheInstance));

        assertEquals(2, apiInstance2.getRelationships().size());
        assertTrue(apiInstance2.hasEfferentRelationshipWith(databaseInstance2));
        assertTrue(apiInstance2.hasEfferentRelationshipWith(cacheInstance));

        // apiInstance1 -> databaseInstance1
        Relationship relationship = apiInstance1.getEfferentRelationshipWith(databaseInstance1);
        assertEquals("Uses", relationship.getDescription());

        // apiInstance2 -> databaseInstance2
        relationship = apiInstance2.getEfferentRelationshipWith(databaseInstance2);
        assertEquals("Uses", relationship.getDescription());
    }

    @Test
    void test_getElement_ThrowsAnException_WhenANullIdIsSpecified() {
        try {
            model.getElement(null);
        } catch (IllegalArgumentException iae) {
            assertEquals("An element ID must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_getElement_ThrowsAnException_WhenAnEmptyIdIsSpecified() {
        try {
            model.getElement(" ");
        } catch (IllegalArgumentException iae) {
            assertEquals("An element ID must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_getElementWithCanonicalName_ThrowsAnException_WhenANullCanonicalNameIsSpecified() {
        try {
            model.getElementWithCanonicalName(null);
        } catch (IllegalArgumentException iae) {
            assertEquals("A canonical name must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_getElementWithCanonicalName_ThrowsAnException_WhenAnEmptyCanonicalNameIsSpecified() {
        try {
            model.getElementWithCanonicalName(" ");
        } catch (IllegalArgumentException iae) {
            assertEquals("A canonical name must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_getElementWithCanonicalName_ReturnsNull_WhenAnElementWithTheSpecifiedCanonicalNameDoesNotExist() {
        assertNull(model.getElementWithCanonicalName("Software System"));
    }

    @Test
    void test_getElementWithCanonicalName_ReturnsTheElement_WhenAnElementWithTheSpecifiedCanonicalNameExists() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Web Application", "Description", "Technology");

        assertSame(softwareSystem, model.getElementWithCanonicalName("SoftwareSystem://Software System"));
        assertSame(container, model.getElementWithCanonicalName("Container://Software System.Web Application"));
    }

    @Test
    void test_addDeploymentNode_ThrowsAnException_WhenADeploymentNodeWithTheSameNameAlreadyExists() {
        model.addDeploymentNode("Amazon AWS", "Description", "Technology");
        try {
            model.addDeploymentNode("Amazon AWS", "Description", "Technology");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A deployment/infrastructure node named 'Amazon AWS' already exists.", iae.getMessage());
        }
    }

    @Test
    void test_addDeploymentNode_ThrowsAnException_WhenAChildDeploymentNodeWithTheSameNameAlreadyExists() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Amazon Web Services");
        deploymentNode.addDeploymentNode("AWS Region");
        try {
            deploymentNode.addDeploymentNode("AWS Region");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A deployment/infrastructure node named 'AWS Region' already exists.", iae.getMessage());
        }
    }

    @Test
    void test_addDeploymentNode_ThrowsAnException_WhenAChildInfrastructureNodeWithTheSameNameAlreadyExists() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Amazon Web Services");
        deploymentNode.addInfrastructureNode("Node");
        try {
            deploymentNode.addDeploymentNode("Node");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A deployment/infrastructure node named 'Node' already exists.", iae.getMessage());
        }
    }

    @Test
    void test_addInfrastructureNode_ThrowsAnException_WhenAChildDeploymentNodeWithTheSameNameAlreadyExists() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Amazon Web Services");
        deploymentNode.addDeploymentNode("Node");
        try {
            deploymentNode.addInfrastructureNode("Node");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A deployment/infrastructure node named 'Node' already exists.", iae.getMessage());
        }
    }

    @Test
    void test_addInfrastructureNode_ThrowsAnException_WhenAChildInfrastructureNodeWithTheSameNameAlreadyExists() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Amazon Web Services");
        deploymentNode.addInfrastructureNode("Node");
        try {
            deploymentNode.addInfrastructureNode("Node");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A deployment/infrastructure node named 'Node' already exists.", iae.getMessage());
        }
    }

    @Test
    void test_setIdGenerator_ThrowsAnException_WhenANullIdGeneratorIsSpecified() {
        try {
            model.setIdGenerator(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An ID generator must be provided.", iae.getMessage());
        }
    }

    @Test
    void test_hydrate() {
        Person person = new Person();
        person.setId("1");
        person.setName("Person");
        model.setPeople(Collections.singleton(person));

        SoftwareSystem softwareSystem = new SoftwareSystem();
        softwareSystem.setId("2");
        softwareSystem.setName("Software System");
        model.setSoftwareSystems(Collections.singleton(softwareSystem));

        Container container = new Container();
        container.setId("3");
        container.setName("Container");
        softwareSystem.setContainers(Collections.singleton(container));

        Component component = new Component();
        component.setId("4");
        component.setName("Component");
        container.setComponents(Collections.singleton(component));

        DeploymentNode deploymentNodeParent = new DeploymentNode();
        deploymentNodeParent.setId("5");
        deploymentNodeParent.setName("Deployment Node - Parent");
        model.setDeploymentNodes(Collections.singleton(deploymentNodeParent));

        DeploymentNode deploymentNodeChild = new DeploymentNode();
        deploymentNodeChild.setId("6");
        deploymentNodeChild.setName("Deployment Node - Child");
        deploymentNodeParent.setChildren(Collections.singleton(deploymentNodeChild));

        ContainerInstance containerInstance = new ContainerInstance();
        containerInstance.setId("7");
        containerInstance.setContainerId("3");
        deploymentNodeChild.setContainerInstances(Collections.singleton(containerInstance));

        Relationship relationship = new Relationship();
        relationship.setId("8");
        relationship.setSourceId("1");
        relationship.setDestinationId("2");
        person.setRelationships(Collections.singleton(relationship));

        model.hydrate();

        assertSame(person, model.getElement("1"));
        assertSame(model, person.getModel());

        assertSame(softwareSystem, model.getElement("2"));
        assertSame(model, softwareSystem.getModel());

        assertSame(container, model.getElement("3"));
        assertSame(model, container.getModel());
        assertSame(softwareSystem, container.getParent());

        assertSame(component, model.getElement("4"));
        assertSame(model, component.getModel());
        assertSame(container, component.getParent());

        assertSame(deploymentNodeParent, model.getElement("5"));
        assertSame(model, deploymentNodeParent.getModel());
        assertNull(deploymentNodeParent.getParent());

        assertSame(deploymentNodeChild, model.getElement("6"));
        assertSame(model, deploymentNodeChild.getModel());
        assertSame(deploymentNodeParent, deploymentNodeChild.getParent());

        assertSame(containerInstance, model.getElement("7"));
        assertSame(model, containerInstance.getModel());
        assertSame(container, containerInstance.getContainer());

        assertSame(relationship, model.getRelationship("8"));
        assertSame(person, relationship.getSource());
        assertSame(softwareSystem, relationship.getDestination());

        // test that new elements take the next ID
        Element element = model.addPerson("New element", "Description");
        assertEquals("9", element.getId());
    }

    @Test
    void test_impliedRelationshipStrategy() {
        // default strategy initially
        assertTrue(model.getImpliedRelationshipsStrategy() instanceof DefaultImpliedRelationshipsStrategy);

        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        assertTrue(model.getImpliedRelationshipsStrategy() instanceof CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy);
    }

    @Test
    void test_setImpliedRelationshipStrategy_ResetsToTheDefaultStrategy_WhenPassedNull() {
        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        model.setImpliedRelationshipsStrategy(null);

        assertTrue(model.getImpliedRelationshipsStrategy() instanceof DefaultImpliedRelationshipsStrategy);
    }

    @Test
    void test_addSoftwareSystemInstance_AllocatesInstanceIdsPerDeploymentNode() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        DeploymentNode deploymentNodeA = model.addDeploymentNode("Deployment Node A", "", "");
        DeploymentNode deploymentNodeB = model.addDeploymentNode("Deployment Node B", "", "");
        SoftwareSystemInstance softwareSystemInstanceA1 = deploymentNodeA.add(softwareSystem);
        SoftwareSystemInstance softwareSystemInstanceA2 = deploymentNodeA.add(softwareSystem);
        SoftwareSystemInstance softwareSystemInstanceB1 = deploymentNodeB.add(softwareSystem);
        SoftwareSystemInstance softwareSystemInstanceB2 = deploymentNodeB.add(softwareSystem);

        assertEquals("SoftwareSystemInstance://Default/Deployment Node A/Software System[1]", softwareSystemInstanceA1.getCanonicalName());
        assertEquals("SoftwareSystemInstance://Default/Deployment Node A/Software System[2]", softwareSystemInstanceA2.getCanonicalName());
        assertEquals("SoftwareSystemInstance://Default/Deployment Node B/Software System[1]", softwareSystemInstanceB1.getCanonicalName());
        assertEquals("SoftwareSystemInstance://Default/Deployment Node B/Software System[2]", softwareSystemInstanceB2.getCanonicalName());
    }

    @Test
    void test_addContainerInstance_AllocatesInstanceIdsPerDeploymentNode() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "", "");
        DeploymentNode deploymentNodeA = model.addDeploymentNode("Deployment Node A", "", "");
        DeploymentNode deploymentNodeB = model.addDeploymentNode("Deployment Node B", "", "");
        ContainerInstance containerInstanceA1 = deploymentNodeA.add(container);
        ContainerInstance containerInstanceA2 = deploymentNodeA.add(container);
        ContainerInstance containerInstanceB1 = deploymentNodeB.add(container);
        ContainerInstance containerInstanceB2 = deploymentNodeB.add(container);

        assertEquals("ContainerInstance://Default/Deployment Node A/Software System.Container[1]", containerInstanceA1.getCanonicalName());
        assertEquals("ContainerInstance://Default/Deployment Node A/Software System.Container[2]", containerInstanceA2.getCanonicalName());
        assertEquals("ContainerInstance://Default/Deployment Node B/Software System.Container[1]", containerInstanceB1.getCanonicalName());
        assertEquals("ContainerInstance://Default/Deployment Node B/Software System.Container[2]", containerInstanceB2.getCanonicalName());
    }

}