package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StylesTests extends AbstractWorkspaceTestBase {

    private Styles styles = new Styles();

    @Test
    void findElementStyle_ReturnsTheDefaultStyle_WhenPassedNull() {
        ElementStyle style = styles.findElementStyle((Element) null);
        assertEquals(Integer.valueOf(450), style.getWidth());
        assertEquals(Integer.valueOf(300), style.getHeight());
        assertEquals("#dddddd", style.getBackground());
        assertEquals("#000000", style.getColor());
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Shape.Box, style.getShape());
        assertNull(style.getIcon());
        assertEquals(Border.Solid, style.getBorder());
        assertEquals("#9a9a9a", style.getStroke());
        assertEquals(Integer.valueOf(100), style.getOpacity());
        assertEquals(true, style.getMetadata());
        assertEquals(true, style.getDescription());
    }

    @Test
    void findElementStyle_ReturnsTheDefaultStyle_WhenNoStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        ElementStyle style = styles.findElementStyle(element);
        assertEquals(Integer.valueOf(450), style.getWidth());
        assertEquals(Integer.valueOf(300), style.getHeight());
        assertEquals("#dddddd", style.getBackground());
        assertEquals("#000000", style.getColor());
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Shape.Box, style.getShape());
        assertNull(style.getIcon());
        assertEquals(Border.Solid, style.getBorder());
        assertEquals("#9a9a9a", style.getStroke());
        assertEquals(Integer.valueOf(100), style.getOpacity());
        assertEquals(true, style.getMetadata());
        assertEquals(true, style.getDescription());
    }

    @Test
    void findElementStyle_ReturnsTheCorrectStyle_WhenStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        element.addTags("Some Tag");

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000").color("#ffffff");
        styles.addElementStyle("Some Tag").color("#0000ff").stroke("#00ff00").shape(Shape.RoundedBox).width(123).height(456);

        ElementStyle style = styles.findElementStyle(element);
        assertEquals(Integer.valueOf(123), style.getWidth());
        assertEquals(Integer.valueOf(456), style.getHeight());
        assertEquals("#ff0000", style.getBackground());
        assertEquals("#0000ff", style.getColor());
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Shape.RoundedBox, style.getShape());
        assertNull(style.getIcon());
        assertEquals(Border.Solid, style.getBorder());
        assertEquals("#00ff00", style.getStroke());
        assertEquals(Integer.valueOf(100), style.getOpacity());
        assertEquals(true, style.getMetadata());
        assertEquals(true, style.getDescription());
    }

    @Test
    void findElementStyle_ReturnsTheCorrectStyleForAnElementInstance_WhenStylesAreDefined() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name");
        softwareSystem.addTags("Some Tag");

        DeploymentNode deploymentNode = model.addDeploymentNode("Server");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000").color("#ffffff");
        styles.addElementStyle("Some Tag").color("#0000ff").stroke("#00ff00").shape(Shape.RoundedBox).width(123).height(456);

        ElementStyle style = styles.findElementStyle(softwareSystemInstance);
        assertEquals(Integer.valueOf(123), style.getWidth());
        assertEquals(Integer.valueOf(456), style.getHeight());
        assertEquals("#ff0000", style.getBackground());
        assertEquals("#0000ff", style.getColor());
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Shape.RoundedBox, style.getShape());
        assertNull(style.getIcon());
        assertEquals(Border.Solid, style.getBorder());
        assertEquals("#00ff00", style.getStroke());
        assertEquals(Integer.valueOf(100), style.getOpacity());
        assertEquals(true, style.getMetadata());
        assertEquals(true, style.getDescription());
    }

    @Test
    void findElementStyle_ReturnsTheDefaultElementSize_WhenTheShapeIsABox() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        element.addTags("Some Tag");

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000").color("#ffffff");
        styles.addElementStyle("Some Tag").shape(Shape.Box);

        ElementStyle style = styles.findElementStyle(element);
        assertEquals(Shape.Box, style.getShape());
        assertEquals(Integer.valueOf(450), style.getWidth());
        assertEquals(Integer.valueOf(300), style.getHeight());
    }

    @Test
    void findElementStyle_ReturnsTheDefaultElementSize_WhenTheShapeIsAPerson() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        element.addTags("Some Tag");

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000").color("#ffffff");
        styles.addElementStyle("Some Tag").shape(Shape.Person);

        ElementStyle style = styles.findElementStyle(element);
        assertEquals(Shape.Person, style.getShape());
        assertEquals(Integer.valueOf(400), style.getWidth());
        assertEquals(Integer.valueOf(400), style.getHeight());
    }

    @Test
    void findRelationshipStyle_ReturnsTheDefaultStyle_WhenPassedNull() {
        RelationshipStyle style = styles.findRelationshipStyle((Relationship) null);
        assertEquals(Integer.valueOf(2), style.getThickness());
        assertEquals("#707070", style.getColor());
        assertTrue(style.getDashed());
        assertEquals(Routing.Direct, style.getRouting());
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Integer.valueOf(200), style.getWidth());
        assertEquals(Integer.valueOf(50), style.getPosition());
        assertEquals(Integer.valueOf(100), style.getOpacity());
    }

    @Test
    void findRelationshipStyle_ReturnsTheDefaultStyle_WhenNoStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        Relationship relationship = element.uses(element, "Uses");
        RelationshipStyle style = styles.findRelationshipStyle(relationship);
        assertEquals(Integer.valueOf(2), style.getThickness());
        assertEquals("#707070", style.getColor());
        assertTrue(style.getDashed());
        assertEquals(Routing.Direct, style.getRouting());
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Integer.valueOf(200), style.getWidth());
        assertEquals(Integer.valueOf(50), style.getPosition());
        assertEquals(Integer.valueOf(100), style.getOpacity());
    }

    @Test
    void findRelationshipStyle_ReturnsTheCorrectStyle_WhenStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        Relationship relationship = element.uses(element, "Uses");
        relationship.addTags("Some Tag");

        styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
        styles.addRelationshipStyle("Some Tag").color("#0000ff");

        RelationshipStyle style = styles.findRelationshipStyle(relationship);
        assertEquals(Integer.valueOf(2), style.getThickness());
        assertEquals("#0000ff", style.getColor());
        assertTrue(style.getDashed());
        assertEquals(Routing.Direct, style.getRouting());
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Integer.valueOf(200), style.getWidth());
        assertEquals(Integer.valueOf(50), style.getPosition());
        assertEquals(Integer.valueOf(100), style.getOpacity());
    }

    @Test
    void findRelationshipStyle_ReturnsTheCorrectStyle_WhenThereIsALinkedRelationship() {
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
    void findRelationshipStyle_ReturnsTheCorrectStyle_WhenThereIsALinkedRelationshipBasedUponAnImpliedRelationship() {
        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Container container1 = softwareSystem.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");
        Container container2 = softwareSystem.addContainer("Container 2");
        Component component2 = container2.addComponent("Component 2");

        Relationship relationship = component1.uses(component2, "Uses");
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
    void addElementStyle_ThrowsAnException_WhenATagIsNotSpecified() {
        try {
            styles.addElementStyle("");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }

        try {
            styles.addElementStyle(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }

        try {
            styles.addElementStyle(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }
    }

    @Test
    void addElementStyleByTag_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");
            styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element style for the tag \"Software System\" already exists.", iae.getMessage());
        }
    }

    @Test
    void addElementStyle_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            ElementStyle style = styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");
            styles.add(style);

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element style for the tag \"Software System\" already exists.", iae.getMessage());
        }
    }

    @Test
    void addRelationshipStyle_ThrowsAnException_WhenATagIsNotSpecified() {
        try {
            styles.addRelationshipStyle("");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }

        try {
            styles.addRelationshipStyle(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }

        try {
            styles.addRelationshipStyle(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }
    }

    @Test
    void addRelationshipStyleByTag_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
            styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship style for the tag \"Relationship\" already exists.", iae.getMessage());
        }
    }

    @Test
    void addRelationshipStyle_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            RelationshipStyle style = styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
            styles.add(style);

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship style for the tag \"Relationship\" already exists.", iae.getMessage());
        }
    }

    @Test
    void clearElementStyles_RemovesAllElementStyles() {
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");
        assertEquals(1, styles.getElements().size());

        styles.clearElementStyles();
        assertEquals(0, styles.getElements().size());
    }

    @Test
    void clearRelationshipStyles_RemovesAllRelationshipStyles() {
        styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
        assertEquals(1, styles.getRelationships().size());

        styles.clearRelationshipStyles();
        assertEquals(0, styles.getRelationships().size());
    }

    @Test
    void getElementStyle_ThrowsAnException_WhenGivenNoTag() {
        try {
            styles.getElementStyle(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }

        try {
            styles.getElementStyle(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }
    }

    @Test
    void getElementStyle_ReturnsNull_WhenThereIsNoStyleForTheGivenTag() {
        ElementStyle style = styles.getElementStyle("Tag");
        assertNull(style);
    }

    @Test
    void getElementStyle_ReturnsTheElementStyle_WhenThereIsAStyleForTheGivenTag() {
        styles.addElementStyle("Tag").background("#ffffff");

        ElementStyle style = styles.getElementStyle("Tag");
        assertEquals("#ffffff", style.getBackground());
    }

    @Test
    void getRelationshipStyle_ThrowsAnException_WhenGivenNoTag() {
        try {
            styles.getRelationshipStyle(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }

        try {
            styles.getRelationshipStyle(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A tag must be specified.", iae.getMessage());
        }
    }

    @Test
    void getRelationshipStyle_ReturnsNull_WhenThereIsNoStyleForTheGivenTag() {
        RelationshipStyle style = styles.getRelationshipStyle("Tag");
        assertNull(style);
    }

    @Test
    void getRelationshipStyle_ReturnsTheRelationshipStyle_WhenThereIsAStyleForTheGivenTag() {
        styles.addRelationshipStyle("Tag").color("#ffffff");

        RelationshipStyle style = styles.getRelationshipStyle("Tag");
        assertEquals("#ffffff", style.getColor());
    }

}