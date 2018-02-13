package com.structurizr.analysis;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SourceCodeComponentFinderStrategyTests {

    private Container webApplication;
    private Component someComponent;
    private File sourcePath = new File("test/unit");

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        webApplication = softwareSystem.addContainer("Name", "Description", "Technology");

        someComponent = webApplication.addComponentAndCode(
                "SomeComponent",
                "test.SourceCodeComponentFinderStrategy.SomeComponent",
                "test.SourceCodeComponentFinderStrategy",
                "", "");
        someComponent.addSupportingCode(
                "SomeComponentImpl",
                "test.SourceCodeComponentFinderStrategy.SomeComponentImpl",
                "test.SourceCodeComponentFinderStrategy");
    }

    @Test
    public void test_findComponents() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "test.SourceCodeComponentFinderStrategy",
                new SourceCodeComponentFinderStrategy(sourcePath)
        );
        componentFinder.findComponents();

        assertEquals("A component that does something.", someComponent.getDescription());
        assertEquals(20, someComponent.getSize());
    }

    @Test
    public void test_findComponents_TruncatesComponentDescriptions_WhenComponentDescriptionsAreTooLong() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "test.SourceCodeComponentFinderStrategy",
                new SourceCodeComponentFinderStrategy(sourcePath, 10)
        );
        componentFinder.findComponents();

        assertEquals("A compo...", someComponent.getDescription());
    }

    @Test
    public void test_findComponents_DoesNotSetTheComponentDescription_WhenTheComponentAlreadyHasADescription() throws Exception {
        someComponent.setDescription("An existing description.");
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "test.SourceCodeComponentFinderStrategy",
                new SourceCodeComponentFinderStrategy(sourcePath, 10)
        );
        componentFinder.findComponents();

        assertEquals("An existing description.", someComponent.getDescription());
    }

}