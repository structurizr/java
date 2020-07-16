package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StyleTests extends AbstractWorkspaceTestBase {

    private Styles styles = new Styles();

    @Test
    public void test_findElementStyle_ReturnsTheDefaultStyle_WhenPassedNull() {
        ElementStyle style = styles.findElementStyle(null);
        assertEquals("#dddddd", style.getBackground());
        assertEquals("#000000", style.getColor());
        assertEquals(Shape.Box, style.getShape());
        assertEquals(new Integer(450), style.getWidth());
        assertEquals(new Integer(300), style.getHeight());
    }

    @Test
    public void test_findElementStyle_ReturnsTheDefaultStyle_WhenNoStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        ElementStyle style = styles.findElementStyle(element);
        assertEquals("#dddddd", style.getBackground());
        assertEquals("#000000", style.getColor());
        assertEquals(Shape.Box, style.getShape());
        assertEquals(new Integer(450), style.getWidth());
        assertEquals(new Integer(300), style.getHeight());
    }

    @Test
    public void test_findElementStyle_ReturnsTheCorrectStyle_WhenStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        element.addTags("Some Tag");

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000").color("#ffffff");
        styles.addElementStyle("Some Tag").color("#0000ff").stroke("#00ff00").shape(Shape.RoundedBox).width(123).height(456);

        ElementStyle style = styles.findElementStyle(element);
        assertEquals("#ff0000", style.getBackground());
        assertEquals("#0000ff", style.getColor());
        assertEquals("#00ff00", style.getStroke());
        assertEquals(Shape.RoundedBox, style.getShape());
        assertEquals(new Integer(123), style.getWidth());
        assertEquals(new Integer(456), style.getHeight());
    }

    @Test
    public void test_findElementStyle_ReturnsTheDefaultElementSize_WhenTheShapeIsABox() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        element.addTags("Some Tag");

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000").color("#ffffff");
        styles.addElementStyle("Some Tag").shape(Shape.Box);

        ElementStyle style = styles.findElementStyle(element);
        assertEquals(Shape.Box, style.getShape());
        assertEquals(new Integer(450), style.getWidth());
        assertEquals(new Integer(300), style.getHeight());
    }

    @Test
    public void test_findElementStyle_ReturnsTheDefaultElementSize_WhenTheShapeIsAPerson() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        element.addTags("Some Tag");

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000").color("#ffffff");
        styles.addElementStyle("Some Tag").shape(Shape.Person);

        ElementStyle style = styles.findElementStyle(element);
        assertEquals(Shape.Person, style.getShape());
        assertEquals(new Integer(400), style.getWidth());
        assertEquals(new Integer(400), style.getHeight());
    }

    @Test
    public void test_findRelationshipStyle_ReturnsTheDefaultStyle_WhenPassedNull() {
        RelationshipStyle style = styles.findRelationshipStyle(null);
        assertEquals("#707070", style.getColor());
    }

    @Test
    public void test_findRelationshipStyle_ReturnsTheDefaultStyle_WhenNoStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        Relationship relationship = element.uses(element, "Uses");
        RelationshipStyle style = styles.findRelationshipStyle(relationship);
        assertEquals("#707070", style.getColor());
    }

    @Test
    public void test_findRelationshipStyle_ReturnsTheCorrectStyle_WhenStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        Relationship relationship = element.uses(element, "Uses");
        relationship.addTags("Some Tag");

        styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
        styles.addRelationshipStyle("Some Tag").color("#0000ff");

        RelationshipStyle style = styles.findRelationshipStyle(relationship);
        assertEquals("#0000ff", style.getColor());
    }

    @Test
    public void test_findRelationshipStyle_ReturnsTheCorrectStyle_WhenThereIsALinkedRelationship() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container1 = softwareSystem.addContainer("Container 1", "Description", "Technology");
        Container container2 = softwareSystem.addContainer("Container 2", "Description", "Technology");
        Relationship relationship = container1.uses(container2, "Uses");
        relationship.addTags("Tag");
        styles.addRelationshipStyle("Tag").color("#0000ff");

        RelationshipStyle style = styles.findRelationshipStyle(relationship);
        assertEquals("#0000ff", style.getColor());

        DeploymentNode deploymentNode = model.addDeploymentNode("Server");
        ContainerInstance containerInstance1 = deploymentNode.add(container1);
        ContainerInstance containerInstance2 = deploymentNode.add(container2);

        Relationship relationshipInstance = containerInstance1.getEfferentRelationshipWith(containerInstance2);

        style = styles.findRelationshipStyle(relationshipInstance);
        assertEquals("#0000ff", style.getColor());
    }

    @Test
    public void test_addElementStyleByTag_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");
            styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element style for the tag \"Software System\" already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_addElementStyle_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            ElementStyle style = styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");
            styles.add(style);

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element style for the tag \"Software System\" already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_addRelationshipStyleByTag_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
            styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship style for the tag \"Relationship\" already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_addRelationshipStyle_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            RelationshipStyle style = styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
            styles.add(style);

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship style for the tag \"Relationship\" already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_clearElementStyles_RemovesAllElementStyles() {
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");
        assertEquals(1, styles.getElements().size());

        styles.clearElementStyles();
        assertEquals(0, styles.getElements().size());
    }

    @Test
    public void test_clearRelationshipStyles_RemovesAllRelationshipStyles() {
        styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
        assertEquals(1, styles.getRelationships().size());

        styles.clearRelationshipStyles();
        assertEquals(0, styles.getRelationships().size());
    }

}