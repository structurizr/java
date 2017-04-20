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
    public void test_addRelationship_AddsARelationshipWithTheSpecifiedDescription() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Relationship relationship = model.addRelationship(a, b, "Uses");

        assertSame(a, relationship.getSource());
        assertSame(b, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertNull(relationship.getTechnology());
        assertEquals(InteractionStyle.Synchronous, relationship.getInteractionStyle());

        assertTrue(model.getRelationships().contains(relationship));
    }

    @Test
    public void test_addRelationship_AddsARelationshipWithTheSpecifiedDescriptionAndTechnology() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Relationship relationship = model.addRelationship(a, b, "Uses", "HTTPS");

        assertSame(a, relationship.getSource());
        assertSame(b, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertEquals("HTTPS", relationship.getTechnology());
        assertEquals(InteractionStyle.Synchronous, relationship.getInteractionStyle());

        assertTrue(model.getRelationships().contains(relationship));
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

    @Test(expected = IllegalArgumentException.class)
    public void test_nullName() {
        model.addSoftwareSystem(Location.External, null, null);
    }

    @Test
    public void test_nullDescription() {
        model.addSoftwareSystem(Location.External, "SoftwareSystem", null);
    }

}
