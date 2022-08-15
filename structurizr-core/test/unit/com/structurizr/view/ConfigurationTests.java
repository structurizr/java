package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTests extends AbstractWorkspaceTestBase {

    @Test
    void defaultView_DoesNothing_WhenPassedNull() {
        Configuration configuration = new Configuration();
        configuration.setDefaultView((View) null);
        assertNull(configuration.getDefaultView());
    }

    @Test
    void defaultView() {
        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        Configuration configuration = new Configuration();
        configuration.setDefaultView(view);
        assertEquals("key", configuration.getDefaultView());
    }

    @Test
    void copyConfigurationFrom() {
        Configuration source = new Configuration();
        source.setLastSavedView("someKey");

        Configuration destination = new Configuration();
        destination.copyConfigurationFrom(source);
        assertEquals("someKey", destination.getLastSavedView());
    }

    @Test
    void setTheme_WithAUrl() {
        Configuration configuration = new Configuration();
        configuration.setTheme("https://example.com/theme.json");
        assertEquals("https://example.com/theme.json", configuration.getTheme());
    }

    @Test
    void setTheme_WithAUrlThatHasATrailingSpace() {
        Configuration configuration = new Configuration();
        configuration.setTheme("https://example.com/theme.json ");
        assertEquals("https://example.com/theme.json", configuration.getTheme());
    }

    @Test
    void setTheme_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            Configuration configuration = new Configuration();
            configuration.setTheme("htt://blah");
        });
    }

    @Test
    void setTheme_DoesNothing_WhenANullUrlIsSpecified() {
        Configuration configuration = new Configuration();
        configuration.setTheme(null);
        assertNull(configuration.getTheme());
    }

    @Test
    void setTheme_DoesNothing_WhenAnEmptyUrlIsSpecified() {
        Configuration configuration = new Configuration();
        configuration.setTheme(" ");
        assertNull(configuration.getTheme());
    }

}