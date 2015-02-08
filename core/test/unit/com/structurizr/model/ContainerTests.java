package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContainerTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System", "Description");
    private Container container = softwareSystem.addContainer("Container", "Description", "Some technology");

    @Test
    public void test_getCanonicalName() {
        assertEquals("/System/Container", container.getCanonicalName());
    }

}
