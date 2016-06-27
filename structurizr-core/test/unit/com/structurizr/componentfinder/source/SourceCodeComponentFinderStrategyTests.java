package com.structurizr.componentfinder.source;

import com.structurizr.Workspace;
import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.SourceCodeComponentFinderStrategy;
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
    private Component componentA, componentB, componentC;
    private File sourcePath = new File("test/unit");

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        webApplication = softwareSystem.addContainer("Name", "Description", "Technology");

        componentA = webApplication.addComponent(
                "ComponentA",
                "com.structurizr.componentfinder.source.componentA.ComponentA",
                "", "");

        componentB = webApplication.addComponent(
                "ComponentB",
                "com.structurizr.componentfinder.source.componentB.ComponentB",
                "", "");

        componentC = webApplication.addComponent(
                "ComponentC",
                "com.structurizr.componentfinder.source.componentC.ComponentC",
                "", "");
    }

    @Test
    public void test_findComponents() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.source",
                new SourceCodeComponentFinderStrategy(sourcePath)
        );
        componentFinder.findComponents();

        assertEquals("A component that does something.", componentA.getDescription());
        assertEquals(10, componentA.getSize());

        assertEquals("A component that does something else.", componentB.getDescription());
        assertEquals(10, componentB.getSize());

        assertEquals("A component that does something else too.", componentC.getDescription());
        assertEquals(10, componentC.getSize());
    }

    @Test
    public void test_findComponents_TruncatesComponentDescriptions_WhenComponentDescriptionsAreTooLong() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.source",
                new SourceCodeComponentFinderStrategy(sourcePath, 32)
        );
        componentFinder.findComponents();

        assertEquals("A component that does something.", componentA.getDescription());
        assertEquals("A component that does somethi...", componentB.getDescription());
        assertEquals("A component that does somethi...", componentC.getDescription());
    }

}
