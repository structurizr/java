package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.*;

public class StaticViewTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_addAnimationStep_ThrowsAnException_WhenNoElementsAreSpecified() {
        try {
            SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
            view.addAnimation();
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("One or more elements must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addAnimationStep() {
        SoftwareSystem element1 = model.addSoftwareSystem("Software System 1", "");
        SoftwareSystem element2 = model.addSoftwareSystem("Software System 2", "");
        SoftwareSystem element3 = model.addSoftwareSystem("Software System 3", "");

        Relationship relationship1_2 = element1.uses(element2, "uses");
        Relationship relationship2_3 = element2.uses(element3, "uses");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();

        view.addAnimation(element1);
        view.addAnimation(element2);
        view.addAnimation(element3);

        Animation step1 = view.getAnimations().stream().filter(step -> step.getOrder() == 1).findFirst().get();
        assertEquals(1, step1.getElements().size());
        assertTrue(step1.getElements().contains(element1.getId()));
        assertEquals(0, step1.getRelationships().size());

        Animation step2 = view.getAnimations().stream().filter(step -> step.getOrder() == 2).findFirst().get();
        assertEquals(1, step2.getElements().size());
        assertTrue(step2.getElements().contains(element2.getId()));
        assertEquals(1, step2.getRelationships().size());
        assertTrue(step2.getRelationships().contains(relationship1_2.getId()));

        Animation step3 = view.getAnimations().stream().filter(step -> step.getOrder() == 3).findFirst().get();
        assertEquals(1, step3.getElements().size());
        assertTrue(step3.getElements().contains(element3.getId()));
        assertEquals(1, step3.getRelationships().size());
        assertTrue(step3.getRelationships().contains(relationship2_3.getId()));
    }

    @Test
    public void test_addAnimationStep_IgnoresElementsThatDoNotExistInTheView() {
        SoftwareSystem element1 = model.addSoftwareSystem("Software System 1", "");
        SoftwareSystem element2 = model.addSoftwareSystem("Software System 2", "");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.add(element1);
        view.addAnimation(element1, element2);

        Animation step1 = view.getAnimations().stream().filter(step -> step.getOrder() == 1).findFirst().get();
        assertEquals(1, step1.getElements().size());
        assertTrue(step1.getElements().contains(element1.getId()));
    }

    @Test
    public void test_addAnimationStep_ThrowsAnException_WhenElementsAreSpecifiedButNoneOfThemExistInTheView() {
        try {
            SoftwareSystem element1 = model.addSoftwareSystem("Software System 1", "");

            SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
            view.addAnimation(element1);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("None of the specified elements exist in this view.", iae.getMessage());
        }
    }

}