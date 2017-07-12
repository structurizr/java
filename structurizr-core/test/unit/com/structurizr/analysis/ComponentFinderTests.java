package com.structurizr.analysis;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Container;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ComponentFinderTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_construction_ThrowsAnException_WhenANullContainerIsSpecified() {
        try {
            new ComponentFinder(null, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenANullPackageNameIsSpecified() {
        try {
            Container container = model.addSoftwareSystem("Software System", "").addContainer("Container", "", "");
            new ComponentFinder(container, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A package name must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenNoComponentFinderStrategiesAreSpecified() {
        try {
            Container container = model.addSoftwareSystem("Software System", "").addContainer("Container", "", "");
            new ComponentFinder(container, "com.mycompany.myapp");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("One or more ComponentFinderStrategy objects must be specified.", iae.getMessage());
        }
    }

}