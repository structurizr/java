package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DynamicViewTests extends AbstractWorkspaceTestBase {

    private Person person;
    private SoftwareSystem softwareSystemA;
    private Container containerA1;
    private Container containerA2;
    private Container containerA3;
    private Component componentA1;
    private Component componentA2;

    private SoftwareSystem softwareSystemB;
    private Container containerB1;
    private Component componentB1;

    private Relationship relationship;

    @Before
    public void setup() {
        person = model.addPerson("Person", "");
        softwareSystemA = model.addSoftwareSystem("Software System A", "");
        containerA1 = softwareSystemA.addContainer("Container A1", "", "");
        componentA1 = containerA1.addComponent("Component A1", "");
        containerA2 = softwareSystemA.addContainer("Container A2", "", "");
        componentA2 = containerA2.addComponent("Component A2", "");
        containerA3 = softwareSystemA.addContainer("Container A3", "", "");
        relationship = containerA1.uses(containerA2, "uses");

        softwareSystemB = model.addSoftwareSystem("Software System B", "");
        containerB1 = softwareSystemB.addContainer("Container B1", "", "");
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsASoftwareSystemButAContainerInAnotherSoftwareSystemIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
            dynamicView.add(containerB1, containerA1);
            fail();
        } catch (Exception e) {
            assertEquals("Only containers that reside inside Software System A can be added to this view.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsASoftwareSystemButAComponentIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
            dynamicView.add(componentA1, containerA1);
            fail();
        } catch (Exception e) {
            assertEquals("Only containers that reside inside Software System A can be added to this view.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsASoftwareSystemAndTheSameSoftwareSystemIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
            dynamicView.add(softwareSystemA, containerA1);
            fail();
        } catch (Exception e) {
            assertEquals("Software System A is already the scope of this view and cannot be added to it.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsAContainerAndTheSameContainerIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(containerA1, "key", "Description");
            dynamicView.add(containerA1, containerA2);
            fail();
        } catch (Exception e) {
            assertEquals("Container A1 is already the scope of this view and cannot be added to it.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsAContainerAndTheParentSoftwareSystemIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(containerA1, "key", "Description");
            dynamicView.add(softwareSystemA, containerA2);
            fail();
        } catch (Exception e) {
            assertEquals("Software System A is already the scope of this view and cannot be added to it.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsAContainerAndAContainerInAnotherSoftwareSystemIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(containerA1, "key", "Description");
            dynamicView.add(containerB1, containerA2);
            fail();
        } catch (Exception e) {
            assertEquals("Only containers that reside inside Software System A can be added to this view.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsAContainerAndAComponentInAnotherContainerIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(containerA1, "key", "Description");
            dynamicView.add(componentA2, containerA2);
            fail();
        } catch (Exception e) {
            assertEquals("Only components that reside inside Container A1 can be added to this view.", e.getMessage());
        }
    }

    @Test
    public void test_add_AddsTheSourceAndDestinationElements_WhenARelationshipBetweenThemExists() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        dynamicView.add(containerA1, containerA2);
        assertEquals(2, dynamicView.getElements().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_add_ThrowsAnException_WhenThereIsNoRelationshipBetweenTheSourceAndDestinationElements() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        dynamicView.add(containerA1, containerA3);
    }

    @Test
    public void test_addRelationshipDirectly() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        dynamicView.add(relationship);
        assertEquals(2, dynamicView.getElements().size());
    }

}
