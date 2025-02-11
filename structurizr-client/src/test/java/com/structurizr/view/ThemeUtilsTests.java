package com.structurizr.view;

import com.structurizr.Workspace;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeUtilsTests {

    @Test
    void loadThemes_DoesNothingWhenNoThemesAreDefined() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        ThemeUtils.loadThemes(workspace);

        // there should still be zero styles in the workspace
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getElements().size());
    }

    @Test
    void loadThemes_LoadsThemesWhenThemesAreDefined() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        softwareSystem.addTags("Amazon Web Services - Alexa For Business");
        workspace.getViews().getConfiguration().setThemes("https://static.structurizr.com/themes/amazon-web-services-2020.04.30/theme.json");

        ThemeUtils.loadThemes(workspace);

        // there should still be zero styles in the workspace
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getElements().size());

        // but we should be able to find a style included in the theme
        ElementStyle style = workspace.getViews().getConfiguration().getStyles().findElementStyle(softwareSystem);
        assertNotNull(style);
        assertEquals("#d6242d", style.getStroke());
        assertEquals("#d6242d", style.getColor());
        assertEquals("https://static.structurizr.com/themes/amazon-web-services-2020.04.30/alexa-for-business.png", style.getIcon());
    }

    @Test
    void toJson() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        assertEquals("{\n" +
                "  \"name\" : \"Name\",\n" +
                "  \"description\" : \"Description\"\n" +
                "}", ThemeUtils.toJson(workspace));

        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.ELEMENT).background("#ff0000");
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
        workspace.getViews().getConfiguration().getBranding().setLogo("https://structurizr.com/static/img/structurizr-logo.png");
        workspace.getViews().getConfiguration().getBranding().setFont(new Font("Open Sans", "https://fonts.googleapis.com/css?family=Open+Sans:400,700"));
        assertEquals("{\n" +
                "  \"name\" : \"Name\",\n" +
                "  \"description\" : \"Description\",\n" +
                "  \"elements\" : [ {\n" +
                "    \"tag\" : \"Element\",\n" +
                "    \"background\" : \"#ff0000\"\n" +
                "  } ],\n" +
                "  \"relationships\" : [ {\n" +
                "    \"tag\" : \"Relationship\",\n" +
                "    \"color\" : \"#ff0000\"\n" +
                "  } ],\n" +
                "  \"logo\" : \"https://structurizr.com/static/img/structurizr-logo.png\",\n" +
                "  \"font\" : {\n" +
                "    \"name\" : \"Open Sans\",\n" +
                "    \"url\" : \"https://fonts.googleapis.com/css?family=Open+Sans:400,700\"\n" +
                "  }\n" +
                "}", ThemeUtils.toJson(workspace));
    }

    @Test
    void findElementStyle_WithThemes() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Element").shape(Shape.RoundedBox);

        // theme 1
        Collection<ElementStyle> elementStyles = new ArrayList<>();
        Collection<RelationshipStyle> relationshipStyles = new ArrayList<>();
        elementStyles.add(new ElementStyle("Element").shape(Shape.Box).background("#000000").color("#ffffff"));
        workspace.getViews().getConfiguration().getStyles().addStylesFromTheme(new Theme(elementStyles, relationshipStyles));

        // theme 2
        elementStyles = new ArrayList<>();
        relationshipStyles = new ArrayList<>();
        elementStyles.add(new ElementStyle("Element").background("#ff0000"));
        workspace.getViews().getConfiguration().getStyles().addStylesFromTheme(new Theme(elementStyles, relationshipStyles));

        ElementStyle style = workspace.getViews().getConfiguration().getStyles().findElementStyle(softwareSystem);
        assertEquals(Integer.valueOf(450), style.getWidth());
        assertEquals(Integer.valueOf(300), style.getHeight());
        assertEquals("#ff0000", style.getBackground()); // from theme 2
        assertEquals("#ffffff", style.getColor()); // from theme 1
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Shape.RoundedBox, style.getShape()); // from workspace
        assertNull(style.getIcon());
        assertEquals(Border.Solid, style.getBorder());
        assertEquals("#b20000", style.getStroke());
        assertEquals(Integer.valueOf(100), style.getOpacity());
        assertEquals(true, style.getMetadata());
        assertEquals(true, style.getDescription());
    }

    @Test
    void findRelationshipStyle_WithThemes() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        Relationship relationship = softwareSystem.uses(softwareSystem, "Uses");
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle("Relationship").dashed(false);

        // theme 1
        Collection<ElementStyle> elementStyles = new ArrayList<>();
        Collection<RelationshipStyle> relationshipStyles = new ArrayList<>();
        relationshipStyles.add(new RelationshipStyle("Relationship").color("#ff0000").thickness(4));
        workspace.getViews().getConfiguration().getStyles().addStylesFromTheme(new Theme(elementStyles, relationshipStyles));

        // theme 2
        elementStyles = new ArrayList<>();
        relationshipStyles = new ArrayList<>();
        relationshipStyles.add(new RelationshipStyle("Relationship").color("#0000ff"));
        workspace.getViews().getConfiguration().getStyles().addStylesFromTheme(new Theme(elementStyles, relationshipStyles));

        RelationshipStyle style = workspace.getViews().getConfiguration().getStyles().findRelationshipStyle(relationship);
        assertEquals(Integer.valueOf(4), style.getThickness()); // from theme 1
        assertEquals("#0000ff", style.getColor()); // from theme 2
        assertFalse(style.getDashed()); // from workspace
        assertEquals(Routing.Direct, style.getRouting());
        assertEquals(Integer.valueOf(24), style.getFontSize());
        assertEquals(Integer.valueOf(200), style.getWidth());
        assertEquals(Integer.valueOf(50), style.getPosition());
        assertEquals(Integer.valueOf(100), style.getOpacity());
    }

    @Test
    void loadThemes_ReplacesRelativeIconReferences() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        softwareSystem.addTags("Amazon Web Services - Alexa For Business");
        workspace.getViews().getConfiguration().setThemes("https://static.structurizr.com/themes/amazon-web-services-2020.04.30/theme.json");

        ThemeUtils.loadThemes(workspace);

        // there should still be zero styles in the workspace
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getElements().size());

        // but we should be able to find a style included in the theme
        ElementStyle style = workspace.getViews().getConfiguration().getStyles().findElementStyle(softwareSystem);
        assertNotNull(style);
        assertEquals("#d6242d", style.getStroke());
        assertEquals("#d6242d", style.getColor());
        assertEquals("https://static.structurizr.com/themes/amazon-web-services-2020.04.30/alexa-for-business.png", style.getIcon());
    }

}