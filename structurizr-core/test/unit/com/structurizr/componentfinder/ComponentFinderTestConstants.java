package com.structurizr.componentfinder;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

public class ComponentFinderTestConstants {

    public static final String MY_APP_TEST_PACKAGE_TO_SCAN = "com.structurizr.componentfinder.myapp";
    public static final String PAPERBOY_APP_PACKAGE_TO_SCAN = "com.structurizr.testapp.paperboy";
    public static final String CONTROLLER_SUFFIX = "Controller";
    public static final String REPOSITORY_SUFFIX = "Repository";
    public static final String JAVA_INTERFACE = "JAVA_INTERFACE";
    public static final String JAVA_CLASS = "JAVA_CLASS";
    public static final String MYAPP_TAG = "MYAPP";
    public static final String PAPERBOY_TAG = "PAPERBOY";

    private ComponentFinderTestConstants() {
    }

    public static Container createDefaultContainer() {
        final Workspace workspace = new Workspace("Name", "Description");
        final Model model = workspace.getModel();
        final SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        return softwareSystem.addContainer("Name", "Description", "Technology");
    }
}
