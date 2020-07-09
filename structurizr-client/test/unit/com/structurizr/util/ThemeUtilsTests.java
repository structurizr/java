package com.structurizr.util;

import com.structurizr.Workspace;
import com.structurizr.model.Tags;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ThemeUtilsTests {

    @Test
    public void test_loadStylesFromThemes() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().setTheme("https://raw.githubusercontent.com/structurizr/themes/master/amazon-web-services/theme.json");

        ThemeUtils.loadStylesFromThemes(workspace);

        assertTrue(workspace.getViews().getConfiguration().getStyles().getElements().size() > 0);
        assertTrue(workspace.getViews().getConfiguration().getStyles().getElements().stream().anyMatch(s -> s.getTag().equals("Amazon Web Services - Cloud")));
    }

    @Test
    public void test_toJson() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        assertEquals("{ }", ThemeUtils.toJson(workspace));

        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.ELEMENT).background("#ff0000");
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle(Tags.RELATIONSHIP).color("#ff0000");
        assertEquals("{\n" +
                "  \"elements\" : [ {\n" +
                "    \"tag\" : \"Element\",\n" +
                "    \"background\" : \"#ff0000\"\n" +
                "  } ],\n" +
                "  \"relationships\" : [ {\n" +
                "    \"tag\" : \"Relationship\",\n" +
                "    \"color\" : \"#ff0000\"\n" +
                "  } ]\n" +
                "}", ThemeUtils.toJson(workspace));
    }

}