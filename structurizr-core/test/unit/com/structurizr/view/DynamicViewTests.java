package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Klaus Lehner
 */
public class DynamicViewTests extends AbstractWorkspaceTestBase {

    private Container container1;
    private Container container2;
    private Container container3;
    private Relationship relationship;
    private SoftwareSystem softwareSystem;

    @Before
    public void setup() {
        softwareSystem = model.addSoftwareSystem("SoftwareSystem", "");

        container1 = softwareSystem.addContainer("Container1", "", "");
        container2 = softwareSystem.addContainer("Container2", "", "");
        container3 = softwareSystem.addContainer("Container3", "", "");

        relationship = container1.uses(container2, "some description");
    }

    @Test
    public void test_addRelationshipWithElements() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystem, "key", "Description");
        dynamicView.add(container1, container2);
        Assert.assertEquals(2, dynamicView.getElements().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_nonExistingRelationShip() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystem, "key", "Description");
        dynamicView.add(container1, container3);
    }

    @Test
    public void test_addRelationshipDirectly() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystem, "key", "Description");
        dynamicView.add(relationship);
        Assert.assertEquals(2, dynamicView.getElements().size());
    }

}
