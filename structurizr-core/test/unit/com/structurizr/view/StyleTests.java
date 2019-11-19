package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
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
    }

    @Test
    public void test_findElementStyle_ReturnsTheDefaultStyle_WhenNoStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        ElementStyle style = styles.findElementStyle(element);
        assertEquals("#dddddd", style.getBackground());
        assertEquals("#000000", style.getColor());
        assertEquals(Shape.Box, style.getShape());
    }

    @Test
    public void test_findElementStyle_ReturnsTheCorrectStyle_WhenStylesAreDefined() {
        SoftwareSystem element = model.addSoftwareSystem("Name", "Description");
        element.addTags("Some Tag");

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000").color("#ffffff");
        styles.addElementStyle("Some Tag").color("#0000ff").borderColor("#00ff00").shape(Shape.RoundedBox);

        ElementStyle style = styles.findElementStyle(element);
        assertEquals("#ff0000", style.getBackground());
        assertEquals("#0000ff", style.getColor());
        assertEquals("#00ff00", style.getBorderColor());
        assertEquals(Shape.RoundedBox, style.getShape());
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
    public void test_addElementStyle_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");
            styles.addElementStyle(Tags.SOFTWARE_SYSTEM).color("#ff0000");

            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element style for the tag \"Software System\" already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_addRelationshipStyle_ThrowsAnException_WhenAStyleWithTheSameTagExistsAlready() {
        try {
            styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
            styles.addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");

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