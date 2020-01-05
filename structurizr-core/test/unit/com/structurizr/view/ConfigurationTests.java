package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConfigurationTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_defaultView_DoesNothing_WhenPassedNull() {
        Configuration configuration = new Configuration();
        configuration.setDefaultView((View)null);
        assertNull(configuration.getDefaultView());
    }

    @Test
    public void test_defaultView() {
        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        Configuration configuration = new Configuration();
        configuration.setDefaultView(view);
        assertEquals("key", configuration.getDefaultView());
    }

    @Test
    public void test_copyConfigurationFrom() {
        Configuration source = new Configuration();
        source.setLastSavedView("someKey");

        Configuration destination = new Configuration();
        destination.copyConfigurationFrom(source);
        assertEquals("someKey", destination.getLastSavedView());
    }

    @Test
    public void test_setTheme_WithAUrl() {
        Configuration configuration = new Configuration();
        configuration.setTheme("https://example.com/theme.json");
        assertEquals("https://example.com/theme.json", configuration.getTheme());
    }

    @Test
    public void test_setTheme_WithAUrlThatHasATrailingSpace() {
        Configuration configuration = new Configuration();
        configuration.setTheme("https://example.com/theme.json ");
        assertEquals("https://example.com/theme.json", configuration.getTheme());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setTheme_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        Configuration configuration = new Configuration();
        configuration.setTheme("htt://blah");
    }

    @Test
    public void test_setTheme_DoesNothing_WhenANullUrlIsSpecified() {
        Configuration configuration = new Configuration();
        configuration.setTheme(null);
        assertNull(configuration.getTheme());
    }

    @Test
    public void test_setTheme_DoesNothing_WhenAnEmptyUrlIsSpecified() {
        Configuration configuration = new Configuration();
        configuration.setTheme(" ");
        assertNull(configuration.getTheme());
    }

}