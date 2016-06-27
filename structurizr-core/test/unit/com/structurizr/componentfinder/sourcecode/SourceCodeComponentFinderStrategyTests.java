package com.structurizr.componentfinder.sourceCode;

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
    private File sourcePath;

    @Before
    public void setUp() {
        if (new File("structurizr-core").exists()) {
            sourcePath = new File("structurizr-core/test/unit");
        } else {
            sourcePath = new File("test/unit");
        }

        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        webApplication = softwareSystem.addContainer("Name", "Description", "Technology");

        componentA = webApplication.addComponent(
                "ComponentA",
                "com.structurizr.componentfinder.sourceCode.componentA.ComponentA",
                "", "");

        componentB = webApplication.addComponent(
                "ComponentB",
                "com.structurizr.componentfinder.sourceCode.componentB.ComponentB",
                "", "");

        componentC = webApplication.addComponent(
                "ComponentC",
                "com.structurizr.componentfinder.sourceCode.componentC.ComponentC",
                "", "");
    }

    @Test
    public void test_findComponents() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.sourceCode",
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
                "com.structurizr.componentfinder.sourceCode",
                new SourceCodeComponentFinderStrategy(sourcePath, 32)
        );
        componentFinder.findComponents();

        assertEquals("A component that does something.", componentA.getDescription());
        assertEquals("A component that does somethi...", componentB.getDescription());
        assertEquals("A component that does somethi...", componentC.getDescription());
    }

}
