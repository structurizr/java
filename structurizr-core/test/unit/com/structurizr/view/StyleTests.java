package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        styles.addElementStyle("Some Tag").color("#0000ff").shape(Shape.RoundedBox);

        ElementStyle style = styles.findElementStyle(element);
        assertEquals("#ff0000", style.getBackground());
        assertEquals("#0000ff", style.getColor());
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

}