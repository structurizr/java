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

}
