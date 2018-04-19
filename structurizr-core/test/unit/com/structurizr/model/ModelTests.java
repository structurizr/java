package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class ModelTests extends AbstractWorkspaceTestBase {

    @Test(expected = IllegalArgumentException.class)
    public void test_addSoftwareSystem_ThrowsAnException_WhenANullNameIsSpecified() {
        model.addSoftwareSystem(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addSoftwareSystem_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        model.addSoftwareSystem(" ", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addPerson_ThrowsAnException_WhenANullNameIsSpecified() {
        model.addPerson(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addPerson_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        model.addPerson(" ", "");
    }

    @Test
    public void test_addSoftwareSystem_AddsTheSoftwareSystem_WhenASoftwareSystemDoesNotExistWithTheSameName() {
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
    public void test_addSoftwareSystem_ThrowsAnException_WhenASoftwareSystemExistsWithTheSameName() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Some description");
        assertEquals(1, model.getSoftwareSystems().size());

        try {
            model.addSoftwareSystem(Location.External, "System A", "Description");
            fail();
        } catch (Exception e) {
            assertEquals("A software system named 'System A' already exists.", e.getMessage());
        }
    }

    @Test
    public void test_addSoftwareSystemWithoutSpecifyingLocation_AddsTheSoftwareSystem_WhenASoftwareSystemDoesNotExistWithTheSameName() {
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
    public void test_addPerson_AddsThePerson_WhenAPersonDoesNotExistWithTheSameName() {
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
    public void test_addPerson_ThrowsAnException_WhenAPersonExistsWithTheSameName() {
        Person person = model.addPerson(Location.Internal, "Admin User", "Description");
        assertEquals(1, model.getPeople().size());

        try {
            model.addPerson(Location.External, "Admin User", "Description");
            fail();
        } catch (Exception e) {
            assertEquals("A person named 'Admin User' already exists.", e.getMessage());
        }
    }

    @Test
    public void test_addPerson_AddsThePersonWithoutSpecifyingTheLocation_WhenAPersonDoesNotExistWithTheSameName() {
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
    public void test_getElement_ReturnsNull_WhenAnElementWithTheSpecifiedIdDoesNotExist() {
        assertNull(model.getElement("100"));
    }

    @Test
    public void test_getElement_ReturnsAnElement_WhenAnElementWithTheSpecifiedIdDoesExist() {
        Person person = model.addPerson(Location.Internal, "Name", "Description");
        assertSame(person, model.getElement(person.getId()));
    }

    @Test
    public void test_contains_ReturnsFalse_WhenTheSpecifiedElementIsNotInTheModel() {
        Model newModel = new Model();
        SoftwareSystem softwareSystem = newModel.addSoftwareSystem(Location.Unspecified, "Name", "Description");
        assertFalse(model.contains(softwareSystem));
    }

    @Test
    public void test_contains_ReturnsTrue_WhenTheSpecifiedElementIsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Unspecified, "Name", "Description");
        assertTrue(model.contains(softwareSystem));
    }

    @Test
    public void test_getSoftwareSystemWithName_ReturnsNull_WhenASoftwareSystemWithTheSpecifiedNameDoesNotExist() {
        assertNull(model.getSoftwareSystemWithName("System X"));
    }

    @Test
    public void test_getSoftwareSystemWithName_ReturnsASoftwareSystem_WhenASoftwareSystemWithTheSpecifiedNameExists() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Description");
        assertSame(softwareSystem, model.getSoftwareSystemWithName("System A"));
    }

    @Test
    public void test_getSoftwareSystemWithId_ReturnsNull_WhenASoftwareSystemWithTheSpecifiedIdDoesNotExist() {
        assertNull(model.getSoftwareSystemWithId("100"));
    }

    @Test
    public void test_getSoftwareSystemWithId_ReturnsASoftwareSystem_WhenASoftwareSystemWithTheSpecifiedIdDoesExist() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Description");
        assertSame(softwareSystem, model.getSoftwareSystemWithId(softwareSystem.getId()));
    }

    @Test
    public void test_getPersonWithName_ReturnsNull_WhenAPersonWithTheSpecifiedNameDoesNotExist() {
        assertNull(model.getPersonWithName("Admin User"));
    }

    @Test
    public void test_getPersonWithName_ReturnsAPerson_WhenAPersonWithTheSpecifiedNameExists() {
        Person person = model.addPerson(Location.External, "Admin User", "Description");
        assertSame(person, model.getPersonWithName("Admin User"));
    }

    @Test
    public void test_addRelationship_AddsARelationshipWithTheSpecifiedDescriptionAndTechnologyAndInteractionStyle() {
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
    public void test_addRelationship_DisallowsTheSameRelationshipToBeAddedMoreThanOnce() {
        SoftwareSystem element1 = model.addSoftwareSystem("Element 1", "Description");
        SoftwareSystem element2 = model.addSoftwareSystem("Element 2", "Description");
        Relationship relationship1 = element1.uses(element2, "Uses", "");
        Relationship relationship2 = element1.uses(element2, "Uses", "");
        assertTrue(element1.has(relationship1));
        assertNull(relationship2);
        assertEquals(1, element1.getRelationships().size());
    }

    @Test
    public void test_addRelationship_AllowsMultipleRelationshipsBetweenElements() {
        SoftwareSystem element1 = model.addSoftwareSystem("Element 1", "Description");
        SoftwareSystem element2 = model.addSoftwareSystem("Element 2", "Description");
        Relationship relationship1 = element1.uses(element2, "Uses in some way", "");
        Relationship relationship2 = element1.uses(element2, "Uses in another way", "");
        assertTrue(element1.has(relationship1));
        assertTrue(element1.has(relationship2));
        assertEquals(2, element1.getRelationships().size());
    }

    @Test
    public void test_modifyRelationship_ThrowsAnException_WhenARelationshipIsNotSpecified() {
        try {
            model.modifyRelationship(null, "Uses", "Technology");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_modifyRelationship_ModifiesAnExistingRelationship_WhenThatRelationshipDoesNotAlreadyExist() {
        SoftwareSystem element1 = model.addSoftwareSystem("Element 1", "Description");
        SoftwareSystem element2 = model.addSoftwareSystem("Element 2", "Description");
        Relationship relationship = element1.uses(element2, "", "");

        model.modifyRelationship(relationship, "Uses", "Technology");
        assertEquals("Uses", relationship.getDescription());
        assertEquals("Technology", relationship.getTechnology());
    }

    @Test
    public void test_modifyRelationship_ThrowsAnException_WhenThatRelationshipDoesAlreadyExist() {
        SoftwareSystem element1 = model.addSoftwareSystem("Element 1", "Description");
        SoftwareSystem element2 = model.addSoftwareSystem("Element 2", "Description");
        Relationship relationship = element1.uses(element2, "Uses", "Technology");

        try {
            model.modifyRelationship(relationship, "Uses", "Technology");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("This relationship exists already: {1 | Element 1 | Description} ---[Uses]---> {2 | Element 2 | Description}", iae.getMessage());
        }
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceAndDestinationAreComponentsInDifferentSoftwareSystems() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa = aa.addComponent("AAA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");
        Component bbb = bb.addComponent("BBB", "", "");

        aaa.uses(bbb, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aaa.hasEfferentRelationshipWith(bbb));

        // AAA->BBB implies AAA->BB AAA->B AA->BBB AA->BB AA->B A->BBB A->BB A->B
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(9, model.getRelationships().size());
        assertEquals(8, implicitRelationships.size());
        assertTrue(aaa.hasEfferentRelationshipWith(bb));
        assertTrue(aa.hasEfferentRelationshipWith(bbb));
        assertTrue(aa.hasEfferentRelationshipWith(bb));
        assertTrue(aaa.hasEfferentRelationshipWith(b));
        assertTrue(a.hasEfferentRelationshipWith(bbb));
        assertTrue(aa.hasEfferentRelationshipWith(b));
        assertTrue(a.hasEfferentRelationshipWith(bb));
        assertTrue(a.hasEfferentRelationshipWith(b));
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceIsAComponentAndDestinationIsAContainerInADifferentSoftwareSystem() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa = aa.addComponent("AAA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");

        aaa.uses(bb, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aaa.hasEfferentRelationshipWith(bb));

        // AAA->BB implies AAA->B AA->BB AA->B A->BB A->B
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(6, model.getRelationships().size());
        assertEquals(5, implicitRelationships.size());
        assertTrue(aa.hasEfferentRelationshipWith(bb));
        assertTrue(aaa.hasEfferentRelationshipWith(b));
        assertTrue(aa.hasEfferentRelationshipWith(b));
        assertTrue(a.hasEfferentRelationshipWith(bb));
        assertTrue(a.hasEfferentRelationshipWith(b));
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceIsAComponentAndDestinationIsADifferentSoftwareSystem() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa = aa.addComponent("AAA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");

        aaa.uses(b, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aaa.hasEfferentRelationshipWith(b));

        // AAA->B implies AA->B A->B
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(3, model.getRelationships().size());
        assertEquals(2, implicitRelationships.size());
        assertTrue(aa.hasEfferentRelationshipWith(b));
        assertTrue(a.hasEfferentRelationshipWith(b));
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceIsAContainerAndDestinationIsAComponentInADifferentSoftwareSystem() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");
        Component bbb = bb.addComponent("BBB", "", "");

        aa.uses(bbb, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aa.hasEfferentRelationshipWith(bbb));

        // AA->BBB implies AA->BB AA->B A->BBB A->BB A->B
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(6, model.getRelationships().size());
        assertEquals(5, implicitRelationships.size());
        assertTrue(aa.hasEfferentRelationshipWith(bb));
        assertTrue(aa.hasEfferentRelationshipWith(b));
        assertTrue(a.hasEfferentRelationshipWith(bbb));
        assertTrue(a.hasEfferentRelationshipWith(bb));
        assertTrue(a.hasEfferentRelationshipWith(b));
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceAndDestinationAreContainersInDifferentSoftwareSystems() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");

        aa.uses(bb, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aa.hasEfferentRelationshipWith(bb));

        // AA->BB implies AA->B A->BB A->B
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(4, model.getRelationships().size());
        assertEquals(3, implicitRelationships.size());
        assertTrue(aa.hasEfferentRelationshipWith(b));
        assertTrue(a.hasEfferentRelationshipWith(bb));
        assertTrue(a.hasEfferentRelationshipWith(b));
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceIsAContainerAndDestinationIsADifferentSoftwareSystem() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");

        aa.uses(b, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aa.hasEfferentRelationshipWith(b));

        // AA->B implies A->B
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(2, model.getRelationships().size());
        assertEquals(1, implicitRelationships.size());
        assertTrue(a.hasEfferentRelationshipWith(b));
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceIsASoftwareSystemAndDestinationIsAComponentInADifferentSoftwareSystem() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");
        Component bbb = bb.addComponent("BBB", "", "");

        a.uses(bbb, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(a.hasEfferentRelationshipWith(bbb));

        // A->BBB implies A->BB A->B
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(3, model.getRelationships().size());
        assertEquals(2, implicitRelationships.size());
        assertTrue(a.hasEfferentRelationshipWith(bb));
        assertTrue(a.hasEfferentRelationshipWith(b));
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceIsASoftwareSystemAndDestinationIsAContainerInADifferentSoftwareSystem() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");

        a.uses(bb, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(a.hasEfferentRelationshipWith(bb));

        // A->BB implies A->B
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(2, model.getRelationships().size());
        assertEquals(1, implicitRelationships.size());
        assertTrue(a.hasEfferentRelationshipWith(b));
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceAndDestinationAreDifferentSoftwareSystems() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");

        a.uses(b, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(a.hasEfferentRelationshipWith(b));

        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(1, model.getRelationships().size());
        assertEquals(0, implicitRelationships.size());
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceAndDestinationAreComponentsInTheSameContainer() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa1 = aa.addComponent("AAA1", "", "");
        Component aaa2 = aa.addComponent("AAA2", "", "");

        aaa1.uses(aaa2, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aaa1.hasEfferentRelationshipWith(aaa2));

        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(1, model.getRelationships().size());
        assertEquals(0, implicitRelationships.size());
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceAndDestinationAreContainersInTheSameContainer() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa1 = a.addContainer("AA1", "", "");
        Container aa2 = a.addContainer("AA2", "", "");

        aa1.uses(aa2, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aa1.hasEfferentRelationshipWith(aa2));

        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(1, model.getRelationships().size());
        assertEquals(0, implicitRelationships.size());
    }

    @Test
    public void test_addImplicitRelationships_WhenSourceAndDestinationAreComponentsInTheDifferentContainersInTheSameSoftwareSystem() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa1 = a.addContainer("AA1", "", "");
        Container aa2 = a.addContainer("AA2", "", "");
        Component aaa1 = aa1.addComponent("AAA1", "", "");
        Component aaa2 = aa2.addComponent("AAA2", "", "");

        aaa1.uses(aaa2, "Uses");
        assertEquals(1, model.getRelationships().size());
        assertTrue(aaa1.hasEfferentRelationshipWith(aaa2));

        // AAA1->AAA2 implies AAA1->AA2 AA1->AAA2 AA1->AA2
        Set<Relationship> implicitRelationships = model.addImplicitRelationships();
        assertEquals(4, model.getRelationships().size());
        assertEquals(3, implicitRelationships.size());
        assertTrue(aaa1.hasEfferentRelationshipWith(aa2));
        assertTrue(aa1.hasEfferentRelationshipWith(aaa2));
        assertTrue(aa1.hasEfferentRelationshipWith(aa2));
    }

    @Test
    public void test_addContainerInstance_ThrowsAnException_WhenANullContainerIsSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
            deploymentNode.add(null);
            fail();
        } catch (Exception e) {
            assertEquals("A container must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addContainerInstance_AddsAContainerInstanceAndReplicatesRelationships_WhenAContainerIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        Container container1 = softwareSystem1.addContainer("Container 1", "Description", "Technology");

        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        Container container2 = softwareSystem2.addContainer("Container 2", "Description", "Technology");

        SoftwareSystem softwareSystem3 = model.addSoftwareSystem("Software System 3", "Description");
        Container container3 = softwareSystem3.addContainer("Container 3", "Description", "Technology");

        container1.uses(container2, "Uses 1", "Technology 1", InteractionStyle.Synchronous);
        container2.uses(container3, "Uses 2", "Technology 2", InteractionStyle.Asynchronous);

        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance1 = deploymentNode.add(container1);
        ContainerInstance containerInstance2 = deploymentNode.add(container2);
        ContainerInstance containerInstance3 = deploymentNode.add(container3);

        assertSame(container2, containerInstance2.getContainer());
        assertEquals(container2.getId(), containerInstance2.getContainerId());
        assertSame(softwareSystem2, containerInstance2.getParent());
        assertEquals("/Software System 2/Container 2[1]", containerInstance2.getCanonicalName());
        assertEquals("Element,Container,Container Instance", containerInstance2.getTags());

        assertEquals(1, containerInstance1.getRelationships().size());
        Relationship relationship = containerInstance1.getRelationships().iterator().next();
        assertSame(containerInstance1, relationship.getSource());
        assertSame(containerInstance2, relationship.getDestination());
        assertEquals("Uses 1", relationship.getDescription());
        assertEquals("Technology 1", relationship.getTechnology());
        assertEquals(InteractionStyle.Synchronous, relationship.getInteractionStyle());

        assertEquals(1, containerInstance2.getRelationships().size());
        relationship = containerInstance2.getRelationships().iterator().next();
        assertSame(containerInstance2, relationship.getSource());
        assertSame(containerInstance3, relationship.getDestination());
        assertEquals("Uses 2", relationship.getDescription());
        assertEquals("Technology 2", relationship.getTechnology());
        assertEquals(InteractionStyle.Asynchronous, relationship.getInteractionStyle());
    }

    @Test
    public void test_addContainerInstance_AddsAContainerInstanceAndDoesNotReplicateRelationships_WhenAContainerIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        Container container1 = softwareSystem1.addContainer("Container 1", "Description", "Technology");

        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        Container container2 = softwareSystem2.addContainer("Container 2", "Description", "Technology");

        SoftwareSystem softwareSystem3 = model.addSoftwareSystem("Software System 3", "Description");
        Container container3 = softwareSystem3.addContainer("Container 3", "Description", "Technology");

        container1.uses(container2, "Uses 1", "Technology 1", InteractionStyle.Synchronous);
        container2.uses(container3, "Uses 2", "Technology 2", InteractionStyle.Asynchronous);

        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance1 = deploymentNode.add(container1, false);
        ContainerInstance containerInstance2 = deploymentNode.add(container2, false);
        ContainerInstance containerInstance3 = deploymentNode.add(container3, false);

        assertSame(container2, containerInstance2.getContainer());
        assertEquals(container2.getId(), containerInstance2.getContainerId());
        assertSame(softwareSystem2, containerInstance2.getParent());
        assertEquals("/Software System 2/Container 2[1]", containerInstance2.getCanonicalName());
        assertEquals("Element,Container,Container Instance", containerInstance2.getTags());

        assertEquals(0, containerInstance1.getRelationships().size());
        assertEquals(0, containerInstance2.getRelationships().size());
    }

    @Test
    public void test_getElement_ThrowsAnException_WhenANullIdIsSpecified() {
        try {
            model.getElement(null);
        } catch (IllegalArgumentException iae) {
            assertEquals("An element ID must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_getElement_ThrowsAnException_WhenAnEmptyIdIsSpecified() {
        try {
            model.getElement(" ");
        } catch (IllegalArgumentException iae) {
            assertEquals("An element ID must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_getElementWithCanonicalName_ThrowsAnException_WhenANullCanonicalNameIsSpecified() {
        try {
            model.getElementWithCanonicalName(null);
        } catch (IllegalArgumentException iae) {
            assertEquals("A canonical name must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_getElementWithCanonicalName_ThrowsAnException_WhenAnEmptyCanonicalNameIsSpecified() {
        try {
            model.getElementWithCanonicalName(" ");
        } catch (IllegalArgumentException iae) {
            assertEquals("A canonical name must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_getElementWithCanonicalName_ReturnsNull_WhenAnElementWithTheSpecifiedCanonicalNameDoesNotExist() {
        assertNull(model.getElementWithCanonicalName("Software System"));
    }

    @Test
    public void test_getElementWithCanonicalName_ReturnsTheElement_WhenAnElementWithTheSpecifiedCanonicalNameExists() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");

        assertSame(model.getElementWithCanonicalName("/Software System"), softwareSystem);
        assertSame(model.getElementWithCanonicalName("Software System"), softwareSystem);

        Container container = softwareSystem.addContainer("Web Application", "Description", "Technology");
        assertSame(container, model.getElementWithCanonicalName("/Software System/Web Application"));
        assertSame(container, model.getElementWithCanonicalName("Software System/Web Application"));
    }

    @Test
    public void test_addImplicitRelationships_SetsTheDescriptionOfThePropagatedRelationship_WhenThereIsOnlyOnePossibleDescription() {
        Person user = model.addPerson("Person", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container webApplication = softwareSystem.addContainer("Web Application", "Description", "Technology");

        user.uses(webApplication, "Uses", "");
        model.addImplicitRelationships();

        assertEquals(2, user.getRelationships().size());

        Relationship relationship = user.getRelationships().stream().filter(r -> r.getDestination() == softwareSystem).findFirst().get();
        assertEquals("Uses", relationship.getDescription());
    }

    @Test
    public void test_addImplicitRelationships_DoeNotSetTheDescriptionOfThePropagatedRelationship_WhenThereIsMoreThanOnePossibleDescription() {
        Person user = model.addPerson("Person", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container webApplication = softwareSystem.addContainer("Web Application", "Description", "Technology");

        user.uses(webApplication, "Does something", "");
        user.uses(webApplication, "Does something else", "");
        model.addImplicitRelationships();

        assertEquals(3, user.getRelationships().size());

        Relationship relationship = user.getRelationships().stream().filter(r -> r.getDestination() == softwareSystem).findFirst().get();
        assertEquals("", relationship.getDescription());
    }

    @Test
    public void test_addImplicitRelationships_SetsTheTechnologyOfThePropagatedRelationship_WhenThereIsOnlyOnePossibleTechnology() {
        Person user = model.addPerson("Person", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container webApplication = softwareSystem.addContainer("Web Application", "Description", "Technology");

        user.uses(webApplication, "Uses", "HTTPS");
        model.addImplicitRelationships();

        assertEquals(2, user.getRelationships().size());

        Relationship relationship = user.getRelationships().stream().filter(r -> r.getDestination() == softwareSystem).findFirst().get();
        assertEquals("HTTPS", relationship.getTechnology());
    }

    @Test
    public void test_addImplicitRelationships_DoeNotSetTheTechnologyOfThePropagatedRelationship_WhenThereIsMoreThanOnePossibleTechnology() {
        Person user = model.addPerson("Person", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container webApplication = softwareSystem.addContainer("Web Application", "Description", "Technology");

        user.uses(webApplication, "Does something", "Some technology");
        user.uses(webApplication, "Does something else", "Some other technology");
        model.addImplicitRelationships();

        assertEquals(3, user.getRelationships().size());

        Relationship relationship = user.getRelationships().stream().filter(r -> r.getDestination() == softwareSystem).findFirst().get();
        assertEquals("", relationship.getTechnology());
    }

    @Test
    public void test_addDeploymentNode_ThrowsAnException_WhenADeploymentNodeWithTheSameNameAlreadyExists() {
        model.addDeploymentNode("Amazon AWS", "Description", "Technology");
        try {
            model.addDeploymentNode("Amazon AWS", "Description", "Technology");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A deployment node named 'Amazon AWS' already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_setIdGenerator_ThrowsAnException_WhenANullIdGeneratorIsSpecified() {
        try {
            model.setIdGenerator(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An ID generator must be provided.", iae.getMessage());
        }
    }

}