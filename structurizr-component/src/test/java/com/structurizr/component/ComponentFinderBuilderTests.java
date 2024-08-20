package com.structurizr.component;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ComponentFinderBuilderTests {

    @Test
    void build_ThrowsAnException_WhenAContainerHasNotBeenSpecified() {
        try {
            new ComponentFinderBuilder().build();
            fail();
        } catch (RuntimeException e) {
            assertEquals("A container must be specified", e.getMessage());
        }
    }

    @Test
    void build_ThrowsAnException_WhenATypeProviderHasNotBeenConfigured() {
        Container container = new Workspace("Name", "Description").getModel().addSoftwareSystem("Software System").addContainer("Container");
        try {
            new ComponentFinderBuilder().forContainer(container).build();
            fail();
        } catch (RuntimeException e) {
            assertEquals("One or more type providers must be configured", e.getMessage());
        }
    }

    @Test
    void build_ThrowsAnException_WhenAComponentFinderStrategyHasNotBeenConfigured() {
        Container container = new Workspace("Name", "Description").getModel().addSoftwareSystem("Software System").addContainer("Container");
        File sources = new File("src/main/java");
        try {
            new ComponentFinderBuilder().forContainer(container).fromSource(sources).build();
            fail();
        } catch (RuntimeException e) {
            assertEquals("One or more component finder strategies must be configured", e.getMessage());
        }
    }

}