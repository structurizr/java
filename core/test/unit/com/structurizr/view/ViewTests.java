package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ViewTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_description() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        View view = new SystemContextView(softwareSystem);
        view.setDescription("Some description");
        assertEquals("Some description", view.getDescription());
    }

    @Test
    public void test_addAllSoftwareSystems_DoesNothing_WhenThereAreNoOtherSoftwareSystemsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        View view = new SystemContextView(softwareSystem);
        assertEquals(1, view.getElements().size());
        view.addAllSoftwareSystems();
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addAllSoftwareSystems_DoesAddAllSoftwareSystems_WhenThereAreSoftwareSystemsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.Unspecified, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.Unspecified, "System B", "Description");
        SoftwareSystem softwareSystemC = model.addSoftwareSystem(Location.Unspecified, "System C", "Description");

        View view = new SystemContextView(softwareSystem);
        view.addAllSoftwareSystems();

        assertEquals(4, view.getElements().size());
        Iterator<ElementView> it = view.getElements().iterator();
        assertSame(softwareSystem, it.next().getElement());
        assertSame(softwareSystemA, it.next().getElement());
        assertSame(softwareSystemB, it.next().getElement());
        assertSame(softwareSystemC, it.next().getElement());
    }

    @Test
    public void test_addSoftwareSystem_DoesNothing_WhenGivenNull() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        View view = new SystemContextView(softwareSystem);
        view.addSoftwareSystem(null);
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addSoftwareSystem_DoesNothing_WhenTheSoftwareSystemIsNotInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        View view = new SystemContextView(softwareSystem);

        Model model2 = new Model();
        SoftwareSystem softwareSystemA = model2.addSoftwareSystem(Location.Unspecified, "System A", "Description");
        view.addSoftwareSystem(softwareSystemA);
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addSoftwareSystem_AddsTheSoftwareSystem_WhenTheSoftwareSystemIsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.Unspecified, "System A", "Description");

        View view = new SystemContextView(softwareSystem);
        view.addSoftwareSystem(softwareSystemA);
        assertEquals(2, view.getElements().size());
        Iterator<ElementView> it = view.getElements().iterator();
        assertSame(softwareSystem, it.next().getElement());
        assertSame(softwareSystemA, it.next().getElement());
    }

    @Test
    public void test_addAllPeople_DoesNothing_WhenThereAreNoPeopleInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");

        View view = new SystemContextView(softwareSystem);
        assertEquals(1, view.getElements().size());

        view.addAllPeople();
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addAllPeople_DoesAddAllPeople_WhenThereArePeopleInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        Person person1 = model.addPerson(Location.Unspecified, "Person 1", "Description");
        Person person2 = model.addPerson(Location.Unspecified, "Person 2", "Description");
        Person person3 = model.addPerson(Location.Unspecified, "Person 3", "Description");

        View view = new SystemContextView(softwareSystem);
        view.addAllPeople();

        assertEquals(4, view.getElements().size());
        Iterator<ElementView> it = view.getElements().iterator();
        assertSame(softwareSystem, it.next().getElement());
        assertSame(person1, it.next().getElement());
        assertSame(person2, it.next().getElement());
        assertSame(person3, it.next().getElement());
    }

    @Test
    public void test_addPerson_DoesNothing_WhenGivenNull() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        View view = new SystemContextView(softwareSystem);
        view.addPerson(null);
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addPerson_DoesNothing_WhenThePersonIsNotInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        View view = new SystemContextView(softwareSystem);

        Model model2 = new Model();
        Person person1 = model2.addPerson(Location.Unspecified, "Person 1", "Description");
        view.addPerson(person1);
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addPerson_AddsThePerson_WhenThPersonIsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        View view = new SystemContextView(softwareSystem);

        Person person1 = model.addPerson(Location.Unspecified, "Person 1", "Description");
        view.addPerson(person1);

        assertEquals(2, view.getElements().size());
        Iterator<ElementView> it = view.getElements().iterator();
        assertSame(softwareSystem, it.next().getElement());
        assertSame(person1, it.next().getElement());
    }

    @Test
    public void test_removeElementsWithNoRelationships_RemovesAllElements_WhenTheViewHasNoRelationshipsBetweenElements() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.Unspecified, "System A", "Description");
        Person person1 = model.addPerson(Location.Unspecified, "Person 1", "Description");

        View view = new SystemContextView(softwareSystem);
        view.addAllSoftwareSystems();
        view.addAllPeople();
        view.removeElementsWithNoRelationships();

        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_removeElementsWithNoRelationships_RemovesOnlyThoseElementsWithoutRelationships_WhenTheViewContainsSomeUnlinkedElements() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.Unspecified, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.Unspecified, "System B", "Description");
        Person person1 = model.addPerson(Location.Unspecified, "Person 1", "Description");
        Person person2 = model.addPerson(Location.Unspecified, "Person 2", "Description");
        View view = new SystemContextView(softwareSystem);

        softwareSystem.uses(softwareSystemA, "uses");
        person1.uses(softwareSystem, "uses");

        view.addAllSoftwareSystems();
        view.addAllPeople();
        assertEquals(5, view.getElements().size());

        view.removeElementsWithNoRelationships();
        assertEquals(3, view.getElements().size());
    }

    @Test
    public void test_copyLayoutInformationFrom() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        Person person = model.addPerson(Location.Unspecified, "Person", "Description");

        View view1 = new SystemContextView(softwareSystem);
        view1.addAllSoftwareSystems();
        view1.addAllPeople();
        view1.findElementView(person).setX(123);
        view1.findElementView(person).setY(456);

        View view2 = new SystemContextView(softwareSystem);
        view2.addAllSoftwareSystems();
        view2.addAllPeople();

        assertEquals(0, view2.findElementView(person).getX());
        assertEquals(0, view2.findElementView(person).getY());
        view2.copyLayoutInformationFrom(view1);
        assertEquals(123, view2.findElementView(person).getX());
        assertEquals(456, view2.findElementView(person).getY());
    }

}
