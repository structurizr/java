package com.structurizr.util;

import com.structurizr.Workspace;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class ThemeUtilsTests {

    @Test
    public void test_loadStylesFromThemes() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().setTheme("https://raw.githubusercontent.com/structurizr/themes/master/amazon-web-services/theme.json");

        ThemeUtils.loadStylesFromThemes(workspace);

        assertTrue(workspace.getViews().getConfiguration().getStyles().getElements().size() > 0);
        assertTrue(workspace.getViews().getConfiguration().getStyles().getElements().stream().anyMatch(s -> s.getTag().equals("Amazon Web Services - Cloud")));
    }

}
