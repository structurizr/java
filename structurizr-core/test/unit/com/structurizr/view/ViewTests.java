package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class ViewTests extends AbstractWorkspaceTestBase {

    @Test
    void construction() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        StaticView view = new SystemContextView(softwareSystem, "key", "Description");
        assertEquals("key", view.getKey());
        assertEquals("Description", view.getDescription());
        assertNull(view.getAutomaticLayout());
    }

    @Test
    void construction_WhenTheViewKeyContainsAForwardSlashCharacter() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        StaticView view = new SystemContextView(softwareSystem, "key/1", "Description");
        assertEquals("key_1", view.getKey());
    }

    @Test
    void addAllSoftwareSystems_DoesNothing_WhenThereAreNoOtherSoftwareSystemsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        assertEquals(1, view.getElements().size());
        view.addAllSoftwareSystems();
        assertEquals(1, view.getElements().size());
    }

    @Test
    void addAllSoftwareSystems_DoesAddAllSoftwareSystems_WhenThereAreSoftwareSystemsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.Unspecified, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.Unspecified, "System B", "Description");
        SoftwareSystem softwareSystemC = model.addSoftwareSystem(Location.Unspecified, "System C", "Description");

        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        view.addAllSoftwareSystems();

        assertEquals(4, view.getElements().size());
        Iterator<ElementView> it = view.getElements().iterator();
        assertSame(softwareSystem, it.next().getElement());
        assertSame(softwareSystemA, it.next().getElement());
        assertSame(softwareSystemB, it.next().getElement());
        assertSame(softwareSystemC, it.next().getElement());
    }

    @Test
    void addSoftwareSystem_ThrowsAnException_WhenGivenNull() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        StaticView view = new SystemContextView(softwareSystem, "context", "Description");

        try {
            view.add((SoftwareSystem) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element must be specified.", iae.getMessage());
        }
    }

    @Test
    void addSoftwareSystem_AddsTheSoftwareSystem_WhenTheSoftwareSystemIsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.Unspecified, "System A", "Description");

        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        view.add(softwareSystemA);
        assertEquals(2, view.getElements().size());
        Iterator<ElementView> it = view.getElements().iterator();
        assertSame(softwareSystem, it.next().getElement());
        assertSame(softwareSystemA, it.next().getElement());
    }

    @Test
    void addAllPeople_DoesNothing_WhenThereAreNoPeopleInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");

        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        assertEquals(1, view.getElements().size());

        view.addAllPeople();
        assertEquals(1, view.getElements().size());
    }

    @Test
    void addAllPeople_DoesAddAllPeople_WhenThereArePeopleInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        Person person1 = model.addPerson(Location.Unspecified, "Person 1", "Description");
        Person person2 = model.addPerson(Location.Unspecified, "Person 2", "Description");
        Person person3 = model.addPerson(Location.Unspecified, "Person 3", "Description");

        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        view.addAllPeople();

        assertEquals(4, view.getElements().size());
        Iterator<ElementView> it = view.getElements().iterator();
        assertSame(softwareSystem, it.next().getElement());
        assertSame(person1, it.next().getElement());
        assertSame(person2, it.next().getElement());
        assertSame(person3, it.next().getElement());
    }

    @Test
    void addPerson_ThrowsAnException_WhenGivenNull() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        try {
            view.add((Person) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element must be specified.", iae.getMessage());
        }
    }

    @Test
    void addPerson_AddsThePerson_WhenThPersonIsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        StaticView view = new SystemContextView(softwareSystem, "context", "Description");

        Person person1 = model.addPerson(Location.Unspecified, "Person 1", "Description");
        view.add(person1);

        assertEquals(2, view.getElements().size());
        Iterator<ElementView> it = view.getElements().iterator();
        assertSame(softwareSystem, it.next().getElement());
        assertSame(person1, it.next().getElement());
    }

    @Test
    void removeElementsWithNoRelationships_RemovesAllElements_WhenTheViewHasNoRelationshipsBetweenElements() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "Software System", "Description");
        Person person = model.addPerson(Location.Unspecified, "Person", "Description");

        StaticView view = views.createSystemLandscapeView("context", "Description");
        view.addAllSoftwareSystems();
        view.addAllPeople();
        view.removeElementsWithNoRelationships();

        assertEquals(0, view.getElements().size());
    }

    @Test
    void removeElementsWithNoRelationships_RemovesOnlyThoseElementsWithoutRelationships_WhenTheViewContainsSomeUnlinkedElements() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.Unspecified, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.Unspecified, "System B", "Description");
        Person person1 = model.addPerson(Location.Unspecified, "Person 1", "Description");
        Person person2 = model.addPerson(Location.Unspecified, "Person 2", "Description");
        StaticView view = new SystemContextView(softwareSystem, "context", "Description");

        softwareSystem.uses(softwareSystemA, "uses");
        person1.uses(softwareSystem, "uses");

        view.addAllSoftwareSystems();
        view.addAllPeople();
        assertEquals(5, view.getElements().size());

        view.removeElementsWithNoRelationships();
        assertEquals(3, view.getElements().size());
    }

    @Test
    void copyLayoutInformationFrom() {
        Workspace workspace1 = new Workspace("", "");
        Model model1 = workspace1.getModel();
        SoftwareSystem softwareSystem1A = model1.addSoftwareSystem("System A", "Description");
        SoftwareSystem softwareSystem1B = model1.addSoftwareSystem("System B", "Description");
        Person person1 = model1.addPerson("Person", "Description");
        Relationship personUsesSoftwareSystem1 = person1.uses(softwareSystem1A, "Uses");

        // create a view with SystemA and Person (locations are set for both, relationship has vertices)
        StaticView staticView1 = new SystemContextView(softwareSystem1A, "context", "Description");
        staticView1.setPaperSize(PaperSize.A3_Landscape);
        staticView1.setDimensions(new Dimensions(123, 456));
        staticView1.add(softwareSystem1B);
        staticView1.getElementView(softwareSystem1B).setX(123);
        staticView1.getElementView(softwareSystem1B).setY(321);
        staticView1.add(person1);
        staticView1.getElementView(person1).setX(456);
        staticView1.getElementView(person1).setY(654);
        staticView1.getRelationshipView(personUsesSoftwareSystem1).setVertices(Arrays.asList(new Vertex(123, 456)));
        staticView1.getRelationshipView(personUsesSoftwareSystem1).setPosition(70);
        staticView1.getRelationshipView(personUsesSoftwareSystem1).setRouting(Routing.Orthogonal);

        // and create a dynamic view, as they are treated slightly differently
        DynamicView dynamicView1 = new DynamicView(model1, "dynamic", "Description");
        dynamicView1.add(person1, "Overridden description", softwareSystem1A);
        dynamicView1.getElementView(person1).setX(111);
        dynamicView1.getElementView(person1).setY(222);
        dynamicView1.getElementView(softwareSystem1A).setX(333);
        dynamicView1.getElementView(softwareSystem1A).setY(444);
        dynamicView1.getRelationshipView(personUsesSoftwareSystem1).setVertices(Arrays.asList(new Vertex(555, 666)));
        dynamicView1.getRelationshipView(personUsesSoftwareSystem1).setPosition(30);
        dynamicView1.getRelationshipView(personUsesSoftwareSystem1).setRouting(Routing.Direct);

        Workspace workspace2 = new Workspace("", "");
        Model model2 = workspace2.getModel();
        // creating these in the opposite order will cause them to get different internal IDs
        SoftwareSystem softwareSystem2B = model2.addSoftwareSystem("System B", "Description");
        SoftwareSystem softwareSystem2A = model2.addSoftwareSystem("System A", "Description");
        Person person2 = model2.addPerson("Person", "Description");
        Relationship personUsesSoftwareSystem2 = person2.uses(softwareSystem2A, "Uses");

        // create a view with SystemB and Person (locations are 0,0 for both)
        StaticView staticView2 = new SystemContextView(softwareSystem2A, "context", "Description");
        staticView2.add(softwareSystem2B);
        staticView2.add(person2);
        assertEquals(0, staticView2.getElementView(softwareSystem2B).getX());
        assertEquals(0, staticView2.getElementView(softwareSystem2B).getY());
        assertEquals(0, staticView2.getElementView(softwareSystem2B).getX());
        assertEquals(0, staticView2.getElementView(softwareSystem2B).getY());
        assertEquals(0, staticView2.getElementView(person2).getX());
        assertEquals(0, staticView2.getElementView(person2).getY());
        assertTrue(staticView2.getRelationshipView(personUsesSoftwareSystem2).getVertices().isEmpty());

        // and create a dynamic view (locations are 0,0)
        DynamicView dynamicView2 = new DynamicView(model2, "dynamic", "Description");
        dynamicView2.add(person2, "Overridden description", softwareSystem2A);

        staticView2.copyLayoutInformationFrom(staticView1);
        assertEquals(PaperSize.A3_Landscape, staticView2.getPaperSize());
        assertEquals(123, staticView2.getDimensions().getWidth());
        assertEquals(456, staticView2.getDimensions().getHeight());
        assertEquals(0, staticView2.getElementView(softwareSystem2A).getX());
        assertEquals(0, staticView2.getElementView(softwareSystem2A).getY());
        assertEquals(123, staticView2.getElementView(softwareSystem2B).getX());
        assertEquals(321, staticView2.getElementView(softwareSystem2B).getY());
        assertEquals(456, staticView2.getElementView(person2).getX());
        assertEquals(654, staticView2.getElementView(person2).getY());
        Vertex vertex1 = staticView2.getRelationshipView(personUsesSoftwareSystem2).getVertices().iterator().next();
        assertEquals(123, vertex1.getX());
        assertEquals(456, vertex1.getY());
        assertEquals(70, staticView2.getRelationshipView(personUsesSoftwareSystem2).getPosition().intValue());
        assertEquals(Routing.Orthogonal, staticView2.getRelationshipView(personUsesSoftwareSystem2).getRouting());

        dynamicView2.copyLayoutInformationFrom(dynamicView1);
        assertEquals(111, dynamicView2.getElementView(person2).getX());
        assertEquals(222, dynamicView2.getElementView(person2).getY());
        assertEquals(333, dynamicView2.getElementView(softwareSystem2A).getX());
        assertEquals(444, dynamicView2.getElementView(softwareSystem2A).getY());
        Vertex vertex2 = dynamicView2.getRelationshipView(personUsesSoftwareSystem2).getVertices().iterator().next();
        assertEquals(555, vertex2.getX());
        assertEquals(666, vertex2.getY());
        assertEquals(30, dynamicView2.getRelationshipView(personUsesSoftwareSystem2).getPosition().intValue());
        assertEquals(Routing.Direct, dynamicView2.getRelationshipView(personUsesSoftwareSystem2).getRouting());
    }

    @Test
    void getName() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        SystemContextView systemContextView = new SystemContextView(softwareSystem, "context", "Description");
        assertEquals("The System - System Context", systemContextView.getName());
    }

    @Test
    void removeElementsThatAreUnreachableFrom_DoesNothing_WhenANullElementIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        view.removeElementsThatAreUnreachableFrom(null);
    }

    @Test
    void removeElementsThatAreUnreachableFrom_DoesNothing_WhenAllElementsCanBeReached() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("System A", "");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("System B", "");

        softwareSystem.uses(softwareSystemA, "uses");
        softwareSystemA.uses(softwareSystemB, "uses");

        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        view.addAllElements();
        assertEquals(3, view.getElements().size());

        view.removeElementsThatAreUnreachableFrom(softwareSystem);
        assertEquals(3, view.getElements().size());
    }

    @Test
    void removeElementsThatAreUnreachableFrom_RemovesOrphanedElements_WhenThereAreSomeOrphanedElements() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("System A", "");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("System B", "");
        SoftwareSystem softwareSystemC = model.addSoftwareSystem("System C", "");

        softwareSystem.uses(softwareSystemA, "uses");
        softwareSystemA.uses(softwareSystemB, "uses");

        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        view.addAllElements();
        assertEquals(4, view.getElements().size());

        view.removeElementsThatAreUnreachableFrom(softwareSystem);
        assertEquals(3, view.getElements().size());
        assertFalse(view.getElements().contains(new ElementView(softwareSystemC)));
    }

    @Test
    void removeElementsThatAreUnreachableFrom_RemovesUnreachableElements_WhenThereAreSomeUnreachableElements() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("System A", "");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("System B", "");

        softwareSystem.uses(softwareSystemA, "uses");
        softwareSystemA.uses(softwareSystemB, "uses");

        StaticView view = new SystemContextView(softwareSystem, "context", "Description");
        view.addAllElements();
        assertEquals(3, view.getElements().size());

        view.removeElementsThatAreUnreachableFrom(softwareSystemB);
        assertEquals(2, view.getElements().size());
        assertFalse(view.getElements().contains(new ElementView(softwareSystemA)));
    }

    @Test
    void removeElementsThatAreUnreachableFrom_DoesntIncludeAllElements_WhenThereIsACyclicGraph() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        Person user = model.addPerson("User", "");

        user.uses(softwareSystem1, "");
        user.uses(softwareSystem2, "");
        softwareSystem1.delivers(user, "");

        StaticView view = new SystemContextView(softwareSystem1, "context", "Description");
        view.addAllElements();
        assertEquals(3, view.getElements().size());

        // this should remove software system 2
        view.removeElementsThatAreUnreachableFrom(softwareSystem1);
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystem1)));
        assertTrue(view.getElements().contains(new ElementView(user)));
    }

    @Test
    void removeRelationship_DoesNothing_WhenNullIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        SoftwareSystem softwareSystem3 = model.addSoftwareSystem("Software System 3", "Description");

        softwareSystem1.uses(softwareSystem2, "Uses");
        softwareSystem2.uses(softwareSystem3, "Uses");
        softwareSystem3.uses(softwareSystem1, "Uses");

        StaticView view = new SystemContextView(softwareSystem1, "context", "Description");
        view.addAllElements();

        assertEquals(3, view.getRelationships().size());
        view.remove((Relationship) null);
    }

    @Test
    void removeRelationship_RemovesARelationship_WhenAValidRelationshipIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        SoftwareSystem softwareSystem3 = model.addSoftwareSystem("Software System 3", "Description");

        Relationship relationship12 = softwareSystem1.uses(softwareSystem2, "Uses");
        Relationship relationship23 = softwareSystem2.uses(softwareSystem3, "Uses");
        Relationship relationship31 = softwareSystem3.uses(softwareSystem1, "Uses");

        StaticView view = new SystemContextView(softwareSystem1, "context", "Description");
        view.addAllElements();

        assertEquals(3, view.getRelationships().size());
        view.remove(relationship31);

        assertEquals(2, view.getRelationships().size());
        assertTrue(view.getRelationships().contains(new RelationshipView(relationship12)));
        assertTrue(view.getRelationships().contains(new RelationshipView(relationship23)));
    }

    @Test
    void setKey_ThrowsAnException_WhenANullKeyIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            new SystemContextView(softwareSystem, null, "Description");
        });
    }

    @Test
    void setKey_ThrowsAnException_WhenAnEmptyKeyIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
            new SystemContextView(softwareSystem, " ", "Description");
        });
    }

    @Test
    void addElement_ThrowsAnException_WhenTheSpecifiedElementDoesNotExistInTheModel() {
        try {
            Workspace workspace = new Workspace("1", "");
            SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");

            SystemLandscapeView view = new Workspace("", "").getViews().createSystemLandscapeView("key", "Description");
            view.add(softwareSystem);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The element named Software System does not exist in the model associated with this view.", iae.getMessage());
        }
    }

    @Test
    void enableAutomaticLayout_EnablesAutoLayoutWithSomeDefaultValues_WhenTrueIsSpecified() {
        SystemLandscapeView view = new Workspace("", "").getViews().createSystemLandscapeView("key", "Description");
        view.enableAutomaticLayout();

        assertNotNull(view.getAutomaticLayout());
        assertEquals(AutomaticLayout.RankDirection.TopBottom, view.getAutomaticLayout().getRankDirection());
        assertEquals(300, view.getAutomaticLayout().getRankSeparation());
        assertEquals(600, view.getAutomaticLayout().getNodeSeparation());
        assertEquals(200, view.getAutomaticLayout().getEdgeSeparation());
        assertFalse(view.getAutomaticLayout().isVertices());
    }

    @Test
    void enableAutomaticLayout_DisablesAutoLayout_WhenFalseIsSpecified() {
        SystemLandscapeView view = new Workspace("", "").getViews().createSystemLandscapeView("key", "Description");
        view.enableAutomaticLayout();
        assertNotNull(view.getAutomaticLayout());

        view.disableAutomaticLayout();
        assertNull(view.getAutomaticLayout());
    }

    @Test
    void enableAutomaticLayout() {
        SystemLandscapeView view = new Workspace("", "").getViews().createSystemLandscapeView("key", "Description");
        view.enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 100, 200, 300, true);

        assertNotNull(view.getAutomaticLayout());
        assertEquals(AutomaticLayout.RankDirection.LeftRight, view.getAutomaticLayout().getRankDirection());
        assertEquals(100, view.getAutomaticLayout().getRankSeparation());
        assertEquals(200, view.getAutomaticLayout().getNodeSeparation());
        assertEquals(300, view.getAutomaticLayout().getEdgeSeparation());
        assertTrue(view.getAutomaticLayout().isVertices());
    }

    @Test
    void addCustomElement_AddsTheCustomElementToTheView() {
        Workspace workspace = new Workspace("", "");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");

        CustomElement box1 = workspace.getModel().addCustomElement("Box 1");
        CustomElement box2 = workspace.getModel().addCustomElement("Box 2");
        box1.uses(box2, "Uses");

        view.add(box1);
        assertEquals(1, view.getElements().size());
        assertEquals(0, view.getRelationships().size());

        view.add(box2);
        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    void addCustomElementWithoutRelationships_AddsTheCustomElementToTheView() {
        Workspace workspace = new Workspace("", "");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");

        CustomElement box1 = workspace.getModel().addCustomElement("Box 1");
        CustomElement box2 = workspace.getModel().addCustomElement("Box 2");
        box1.uses(box2, "Uses");

        view.add(box1);
        assertEquals(1, view.getElements().size());
        assertEquals(0, view.getRelationships().size());

        view.add(box2, false);
        assertEquals(2, view.getElements().size());
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void removeCustomElement_RemovesTheCustomElementFromTheView() {
        Workspace workspace = new Workspace("", "");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");

        CustomElement box1 = workspace.getModel().addCustomElement("Box 1");

        view.add(box1);
        assertEquals(1, view.getElements().size());

        view.remove(box1);
        assertEquals(0, view.getElements().size());
    }

}