package com.structurizr.componentfinder;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

public class TestConstants {

    private TestConstants() {
    }

    public static Container createDefaultContainer() {
        final Workspace workspace = new Workspace("Name", "Description");
        final Model model = workspace.getModel();
        final SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        return softwareSystem.addContainer("Name", "Description", "Technology");
    }
}
